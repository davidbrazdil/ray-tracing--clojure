(ns raytracing.object-common
	(:require [raytracing.math :as math])
	(:require [raytracing.geometry :as geometry])
	(:require [raytracing.material :as material]))

(defprotocol PObject
	"Interface that each object in the scene has to implement"
	(debug [ this ])
	(translate [ this v ]
		"Translates the object by vector v")
	(rotateX [ this angle ])
	(rotateY [ this angle ])
	(rotateZ [ this angle ])
	(scale [ this amount ])
	(flip-normal [ this ])
	(bounding-box [ this ])
	(closest-node [ this ray ]
		"Returns a hashmap with two keys - :object is the closest non-composite object in the ray,
		 :distance its distance from the origin of the ray in the multiple of the direction vector
		 lengths. If there isn't such node (i.e. the ray doesn't intersect anything, function
		 returns nil.")
	(intersect [ this ray ] 
		"Returns list of points of intersection of the object with given ray.
		 For convenience these are returned as a single scalar, which is
		 the parameter \"t\" in the line equation: X(t) = P + t * D ")
	(colour-at [ this root-object lights ray ]
		"Takes the first intersection with the object and computes
		 its color in that point"))

(defn first-intersection
	[ object ray ]
	(reduce 	#(if (<= %2 math/EPSILON)
					%1
					(if (nil? %1)
						%2
						(min %1 %2)))
				nil
				(.intersect object ray)))

(defn is-intersected
	"Returns whether there is an intersection with any object between two points"
	[ object v1 v2 ]
	(let [ 	direction				(geometry/vec-subtract v2 v1)
			intersection-first 		(first-intersection
										object
										(geometry/ray-create 	v1 
																direction)) ]
		(and 	(not (nil? intersection-first))
				(< 	intersection-first (geometry/vec-length direction)))))
