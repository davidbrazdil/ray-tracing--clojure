(ns raytracing.main
	(:require [raytracing.autofocus :as autofocus])
	(:require [raytracing.drawing :as drawing])
	(:require [raytracing.math :as math])
	(:require [raytracing.geometry :as geometry])
	(:require [raytracing.material :as material])
	(:require [raytracing.lighting :as lighting])
	(:require [raytracing.output :as output])
	(:require [raytracing.network :as network])
	(:require [raytracing.object :as object]))

(defmacro dbg [x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(def projection	(drawing/projection-create
					(java.lang.Math/toRadians 60)
					8.571628677317875
					800
					600
					(drawing/camera-create
						(geometry/vec-create -1.0 2.0 -5.0)
						(geometry/vec-create -4.0 1.0  5.0))
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

(def light1		(lighting/light-create
					(geometry/vec-create 6 8 -4)
					material/colour-white))

(def scene 		(object/composite-create [ sphere box1 box2 chessboard ]))

(def lights [ light1 ])

(def focus  				(autofocus/focus-on projection sphere))
(def projection-focused 	(drawing/projection-move-screen projection (:screen-distance focus)))
(def drawing-dof-focused 	(drawing/get-fn-dof 5 (:diameter focus) (drawing/get-fn-antialiased 4)))

(def computer-localhost (network/computer-create "localhost" "127.0.0.1" 1099))
(def computer-pwf (network/computer-create "linux.pwf.cl.cam.ac.uk" "193.60.95.68" 1099))
(def computer-srcf (network/computer-create "shell.srcf.net" "131.111.179.83" 1099))
; (def computer-strelec (network/computer-create "strelec" "89.102.181.190" 9999))
(def computers [ computer-localhost computer-pwf computer-srcf ])

(defn draw-network []
	(do 
		(network/check-computers computers)
		(network/generate-pixels 
			scene 
			lights 
			projection 
			computers)))

(defn draw-local [ ]
	(drawing/generate-pixels 
		scene 
		lights 
		projection 
		; (drawing/get-fn-classic)))
		; (drawing/get-fn-dof 5 0.1 (drawing/get-fn-antialiased 4))))
		drawing-dof-focused))

(defn test-save [ pixels ]
	(output/png		"test.png"
					projection
					pixels))

(defn test-realtime [ pixels ]
	(output/realtime 	projection
						pixels))

(defn test-realtime-save [ pixels ]
	(do
		(test-realtime pixels)
		(test-save pixels)))