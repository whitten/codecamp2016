(ns de.blubberquark.twitterbotics
  (:import [twitterbotics KnowledgeBaseModule PersonOfInterest]
           [javax.imageio ImageIO])
  (:require [clojure.data.csv :as csv]
            [clojure.data.json :as json]
            [clj-http.lite.client :as client]
            [clojure.java.io :as io]))


(def imgur-endpoint "https://api.imgur.com/3/")

(def single-value-attr #{:Character :Gender :Politics (keyword "Fictive Status")})

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

(people "Batman")

people

(defn search-imgur [query]
  (client/get "https://api.imgur.com/3/gallery/search/top/1.json"
              {:headers {"Authorization" "Client-ID 69014a344347ae9"}
               :query-params {:q query}
               :accept :json}))

(defn download-imgs [query]
  (doall
   (for [imgur
         (take 10 ((json/read-str (:body (search-imgur query))) "data"))]
     (let [img (ImageIO/read (new java.net.URL (imgur "link")))] img))))

(def wayne-imgs (download-imgs "John Wayne"))
(prn wayne-imgs)

(defn pixels [img]
  (for [i (range 0 (.getHeight img) 15)
        j (range 0 (.getWidth img) 15)
          :let [argb (.getRGB img j i)
                [r g b a] (int->rgba argb)]
          :when (and (> a 20) (> (+ r g b) 1) (< (+ r g b) 760))]
    [j i r g b a]))

(defn draw [image]
  (let [canvas (proxy [javax.swing.JLabel] []
                 (paint [g] (.drawImage g image 0 0 this)))]
    (doto (javax.swing.JFrame.)
      (.add canvas)
      (.setSize (java.awt.Dimension. (.getWidth image) (.getHeight image)))
      (.show))))

(prn (dominant-color (mapcat img->colors (take 5 sun-imgs))))

(draw ((vec sun-imgs) 6))

(prn sun-imgs)

(prn 2)
