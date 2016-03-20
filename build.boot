(set-env!
  :dependencies '[[adzerk/boot-cljs              "1.7.228-1"]
                  [adzerk/boot-reload                "0.4.5"]
                  [hoplon/boot-hoplon               "0.1.13"]
                  [hoplon/hoplon             "6.0.0-alpha13"]
                  [org.clojure/clojure               "1.7.0"]
                  [org.clojure/clojurescript       "1.7.228"]
                  [tailrecursion/boot-jetty          "0.1.3"]
                  ;[com.andrewmcveigh/cljs-time       "0.4.0"]
                  [cljsjs/moment                  "2.10.6-3"]
                  [cljsjs/boot-cljsjs               "0.5.0" ]
                  [exicon/semantic-ui       "2.1.4-SNAPSHOT"]
]
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]])

(deftask dev
  "Build project01 for local development."
  []
  (comp
    (watch)
    ;(speak)
    (from-cljsjs :profile :development)
    (sift :to-resource #{#"semantic-ui.inc.css"})
    (sift :move {#"^semantic-ui.inc.css$" "semantic-ui.css"})
    (sift :to-resource #{#"themes"})
    (hoplon)
    (reload)
    (cljs :optimizations :none
          :source-map true)
    (serve :port 8000)))

(deftask prod
  "Build project01 for production deployment."
  []
  (comp
   (from-cljsjs :profile :production)
   (sift :to-resource #{#"semantic-ui.min.inc.css"})
   (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})
   (sift :to-resource #{#"themes"})
   (hoplon)
   (cljs :optimizations :advanced)
   ;(prerender)
   ))

(deftask make-war
  "Build a war for deployment"
  []
  (comp
      (from-cljsjs :profile :production)
      (sift :to-resource #{#"semantic-ui.min.inc.css"})
      (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})
      (hoplon)
      (cljs :optimizations :advanced)
      (uber :as-jars true)
      ;(web :serve 'castra-notify-chat.handler/app)
      (war)))
