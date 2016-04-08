(ns de.blubberquark.twitterbotics
  (:import
   [twitterbotics KnowledgeBaseModule PersonOfInterest StoryDB]
   [processing.core PImage PConstants]
   [javax.imageio ImageIO])
  (:require
   [quil.core :as quil]
   [clojure.data.csv :as csv]
   [clojure.data.json :as json]
   [clj-http.lite.client :as client]
   [clojure.java.io :as io]))

(def imgur-endpoint "https://api.imgur.com/3/")

(def single-value-attr #{:Character :Gender :Politics (keyword "Fictive Status")})

(prn '[NOC List Stuff])


(defn parse-attribute-value [attribute value]
  [attribute (cond (= value "") nil
                   (single-value-attr attribute) value
                   :else (clojure.string/split value #",[ ]*"))])

(let [kdir "TSV Lists/"
      tdir "output/"
      poi (PersonOfInterest. kdir)]
  (with-open [in-file (io/reader (str kdir "Veale's The NOC List.txt"))]
    (let [people-tab (csv/read-csv in-file :separator \tab :quote \|)
          people-cols (vec (map keyword (first people-tab)))
          people-map (into {}
                       (for [row (rest people-tab)]
                         (let [struct
                               (into {}
                                     (map parse-attribute-value people-cols row))]
                           [(:Character struct) struct])))]
      (def people people-map))))

(prn '[Image Processing])

(defn int->rgba [argb]
  (let [a (bit-and (bit-shift-right argb 24) 0xff)
        r (bit-and (bit-shift-right argb 16) 0xff)
        g (bit-and (bit-shift-right argb 8) 0xff)
        b (bit-and argb 0xff)]
    [r g b a]))

(defn rgba->int [[r g b a]]
  (+ b
     (bit-shift-left g 8)
     (bit-shift-left r 16)
     (bit-shift-left a 24)))

(defn img->colors [img] 
  (for [i (range 0 (.getHeight img) 15)
        j (range 0 (.getWidth img) 15)
        :let [argb (.getRGB img j i)
              [r g b a] (int->rgba argb)]
        :when (and (> a 20) (> (+ r g b) 1) (< (+ r g b) 760))]
    [r g b]))

(defn pixels [img]
  (for [i (range (.getHeight img))
        j (range (.getWidth img))
          :let [argb (.getRGB img j i)
                [r g b a] (int->rgba argb)]
          :when (and (> a 20) (> (+ r g b) 1) (< (+ r g b) 760))]
    [j i r g b a]))

(defn pixdistance [p1 p2]
  (reduce + (map (comp #(* % %) -) p1 p2)))

(defn v3+ [[a b c] [x y z]]
  [(+ a x) (+ b y) (+ c z)])

(defn color-kmeans
  ([pixels] (color-kmeans pixels 
                          (for [i (range 10)] 
                            [(rand-int 255)(rand-int 255)(rand-int 255)])
                          50))
  ([pixels colors n]
   (prn n colors)
   (if (zero? n) colors
       (let [clusters (group-by #(apply min-key 
                                        (partial pixdistance %) 
                                        colors) pixels)
             new-colors (for [color colors]
                          (if (not (clusters color))
                            [(rand-int 255) (rand-int 255) (rand-int 255)]
                            (let [pixels (clusters color)
                                  n (count pixels)
                                  sum (apply map + pixels)
                                  div (map (partial * (/ 1.0 n)) sum)]
                              (vec div))))]
         (recur pixels new-colors (dec n))))))

(defn dominant-color [pixels]
  (let [colors (color-kmeans pixels)
        clusters (group-by #(apply min-key 
                                   (partial pixdistance %) 
                                   colors) pixels)
        
        biggest (apply max-key count (vals clusters))
        n (count biggest)
        sum (reduce v3+ [0.0 0.0 0.0] biggest)
        [sr sb sg] sum
        [r g b] [(int (/ sr n)) (int (/ sb n)) (int (/ sg n))]]
    [r g b]))

(defn scale-color [[r g b] [r1 g1 b1]]
  (let [bright
        (+ (* r1 r) (* g1 g) (* b1 b))]
    [(* r bright) (* g bright) (* b bright)]))

(defn scale-image [img color]
  (let [[r g b] color
        [r1 g1 b1] (for [comp color] (/ color 255.0))]
    (for [[x y r g b a] (pixels img)]
      (.setRGB  x y (scale-color [r g b] [r1 g1 b1])))))

(prn '[Web Resouces])

(defn search-imgur [query]
  (client/get "https://api.imgur.com/3/gallery/search/top/1.json"
              {:headers {"Authorization" "Client-ID 69014a344347ae9"}
               :query-params {:q query}
               :accept :json}))

(defn search-oca [query]
  (client/get "https://openclipart.org/search/json/"
              {:query-params {:query query}
               :accept :json}))

(defn download-img [query]
  (prn query)
  (let [imgur-response (json/read-str (:body (search-imgur query)))
        ;_ (clojure.pprint/pprint (imgur-response "data"))
        oca-response (json/read-str (:body (search-oca query)))
        imgur-hit (-> imgur-response
                      (get  "data")
                      ((partial filter (comp (partial not)
                                             #(get %1 "is_album"))))
                      (first)
                      (get "link"))
        oca-hit (-> oca-response
                    (get "payload")
                    (first)
                    (get "svg")
                    (get "png_full_lossy"))
        _ (prn 'hits oca-hit imgur-hit)
        url (java.net.URL. (or imgur-hit oca-hit imgur-hit))]
    (prn 'CHOSEN url)
    (let [img (ImageIO/read url)]
      (prn img)
      img)))

(prn '[quil Processing])

(defn show-in-frame [image]
  (let [canvas (proxy [javax.swing.JLabel] []
                 (paint [g] (.drawImage g image 0 0 this)))]
    (doto (javax.swing.JFrame.)
      (.add canvas)
      (.setSize (java.awt.Dimension. (.getWidth image) (.getHeight image)))
      (.show))))

(defn to-pimg [bimg]
  (let [pimg (new PImage (.getWidth bimg) (.getHeight bimg ) PConstants/ARGB)];
    (.getRGB bimg 0 0 (.-width pimg) (.-height pimg) (.-pixels pimg) 0  (.-width pimg))
    (.updatePixels pimg)
    pimg))

;(prn (dominant-color (mapcat img->colors (take 5 sun-imgs))))
;(show-in-frame (first (download-imgs "Batman")))
;(draw ((vec hugh-imgs) 0))
;(prn sun-imgs)

(def img (ref nil))

(defn setup []
  (quil/background 0)
  (dosync (ref-set img (to-pimg (download-img "Batman")))))

(defn draw []
  (quil/image @img 0 0))

(quil/defsketch example
  :title "image demo"
  :setup setup
  :draw draw
  :size [800 600])

(prn '[Story Integration])

(let [kdir "TSV Lists/"
       story-db (StoryDB. kdir)]
  (.generateAntagonists story-db)
  (let [_ (prn (bean story-db))
        story-vec (:wordBank
                   (bean story-db))
        [person-A person-B place & verbs] story-vec]
    (prn story-vec)
    (show-in-frame (download-img person-A))
    (show-in-frame (download-img person-B))
    (show-in-frame (download-img place))))

(show-in-frame (download-img "Jack The Ripper"))
(show-in-frame (download-img "Nikola Tesla"))
(show-in-frame (download-img "Voltaire"))

(download-img "Batman")
