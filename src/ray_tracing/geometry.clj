(ns ray-tracing.geometry)

(defrecord Vector [ #^Double x
					#^Double y
					#^Double z ])

(defrecord Ray [ #^Vector point 
			     #^Vector direction ])

(defn vec-create 
	[ x y z ]
	(Vector. x y z))

(defn vec-add
	"Adds two vectors"
	[ v1 v2 ]
	(vec-create	   (+ (:x v1) (:x v2))
	               (+ (:y v1) (:y v2))
	               (+ (:z v1) (:z v2))))

(defn vec-subtract
	"Subtracts two vectors"
	[ v1 v2 ]
	(vec-create	   (- (:x v1) (:x v2))
	               (- (:y v1) (:y v2))
	               (- (:z v1) (:z v2))))

(defn vec-mult
	"Multiplies a vector by a scalar"
	[ v k ]
	(vec-create	   (* k (:x v))
	               (* k (:y v))
	               (* k (:z v))))

(defn vec-length
	"Returns the length of a vector"
	[ v ]
	(java.lang.Math/sqrt
		(+  (* (:x v) (:x v)) 
			(* (:y v) (:y v)) 
			(* (:z v) (:z v)))))

(defn vec-normalize
	"Returns unit length vector"
	[ v ]
	(vec-mult v (/ 1 (vec-length v))))

(defn vec-dot-product
	"Returns the dot product of two vectors"
	[ v1 v2 ]
	(+  (* (:x v1) (:x v2)) 
		(* (:y v1) (:y v2)) 
		(* (:z v1) (:z v2))))

(defn vec-vector-product
	"Returns the vector product of two vectors"
	[ v1 v2 ]
	(vec-create	   	(- 	(* (:y v1) (:z v2))
						(* (:y v2) (:z v1)))
					(- 	(* (:z v1) (:x v2))
					  	(* (:z v2) (:x v1)))
					(- 	(* (:x v1) (:y v2))
					  	(* (:x v2) (:y v1)))))

; (defn matrix-transpose
; 	[ s ]
; 	(apply map vector s))
 
; (defn- nested-for
; 	[ f x y ]
; 	(map 	(fn [a]
; 				(map (fn [b] 
;                 	(f a b)) y))
; 			x))
 
; (defn matrix-mult
; 	[ a b ]
; 	(nested-for 
; 		(fn [x y] (reduce + (map * x y))) a (matrix-transpose b)))

; (defn vec-to-matrix
; 	[ v ]
; 	[ [(:x v)] [(:y v)] [(:z v)] ])

(defn vec-rotate-z
	"Rotation around the z-axis"
	[ v angle ]
	(let [ angle-sin	(java.lang.Math/sin angle)
	       angle-cos	(java.lang.Math/cos angle) ]
		(vec-create
			(- (* (:x v) angle-cos) (* (:y v) angle-sin))
			(+ (* (:x v) angle-sin) (* (:y v) angle-cos))
			(:z v))))

(defn vec-rotate
	"Rotates the vector around an arbitrary axis (intersecting the origin)"
	[ v1 axis angle ]
	(let [ x			(:x v1)
	       y			(:y v1)
	       z			(:z v1)
		   u 			(:x axis)
	       v 			(:y axis)
	       w 			(:z axis)
	       angle-sin	(java.lang.Math/sin angle)
	       angle-cos	(java.lang.Math/cos angle)
	       angle-1Mcos	(- 1 angle-cos)
	       uxPvyPwz		(+ (* u x) (* v y) (* w z)) ]
		(vec-create
			(+ 	(* u uxPvyPwz angle-1Mcos) 
				(* x angle-cos) 
				(* 
					(- (* v z) (* w y) )
					angle-sin))
			(+ 	(* v uxPvyPwz angle-1Mcos) 
				(* y angle-cos) 
				(* 
					(- (* w x) (* u z) )
					angle-sin))
			(+ 	(* w uxPvyPwz angle-1Mcos) 
				(* z angle-cos) 
				(* 
					(- (* u z) (* v x) )
					angle-sin)))))

(defn ray-create
	[ point direction ]
	(Ray. point (vec-normalize direction)))

(defn ray-point
	"Returns point on the ray line given by the multiple of lengths of
	 the direction vector from the starting point (which is normalized,
	 so technically units)."
	 [ ray param ]
	 (vec-add
	 	(:point ray)
	 	(vec-mult
	 		(:direction ray)
	 		param)))
