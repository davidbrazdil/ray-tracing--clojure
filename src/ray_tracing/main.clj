(ns ray-tracing.main
	(:require [ray-tracing.drawing :as drawing])
	(:require [ray-tracing.math :as math])
	(:require [ray-tracing.geometry :as geometry])
	(:require [ray-tracing.material :as material])
	(:require [ray-tracing.lighting :as lighting])
	(:require [ray-tracing.output :as output])
	(:require [ray-tracing.object :as object]))

(def projection	(drawing/projection-create
					(java.lang.Math/toRadians 60)
					2
					1920
					1440					
					(drawing/camera-create
						(geometry/vec-create 1.0 1.9 -3.7)
						(geometry/vec-create -5 1.8 5)
						(geometry/vec-create 0 1 0))					
					material/colour-pastel-light-blue))

(def sphere 	(object/sphere-create
					(geometry/vec-create -0.5 0.6 4)
					0.6
					(material/material-create-simple
						material/colour-pastel-cyan )))

(def box1		(object/box-create
					(geometry/vec-create  -2.5   0   5.5)
 					2.5
 					1.4
 					1
 					(material/material-create-simple
 						material/colour-pastel-brown)))

(def box2		(object/box-create
					(geometry/vec-create  -4.2   0   0.6)
 					1.8
 					1.8
 					1.8
 					(material/material-create-simple
 						material/colour-pastel-blue)))


(def chessboard 	(object/chessboard-create 
						(geometry/vec-create -8 0 0)
						8
						8
						0.2
						(material/material-create-simple material/colour-pastel-light-gray)
						(material/material-create-simple material/colour-pastel-white)))

; (def floor 		(.debug
; 				(.translate 
; 					(.debug
; 					(.rotateX
; 						(.debug
; 						(object/rectangle-create-normal
; 							10
; 							20
; 							geometry/vec-z-neg
; 							(material/material-create-simple
; 								material/colour-gray)))
; 						math/PIover2))
; 					(geometry/vec-create -5 -1 0))))

(def light1		(lighting/light-create
					(geometry/vec-create 6 8 -4)
					material/colour-white))

(def scene 		(object/composite-create [ sphere box1 box2 chessboard ]))

(def lights [ light1 ])

(defn test-draw []
	(drawing/generate-pixels scene lights projection (drawing/get-fn-antialiased 4)))

(defn test-save []
	(output/png		"test.png"
					projection
					(test-draw)))

(defn test-realtime []
	(output/realtime 	projection
						(test-draw)))
