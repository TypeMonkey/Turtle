(data Dat 
 (
   a:int
   b:bool
   c:string
   d:Dat
 )
)


(let ((a:Dat (Dat 10 true 'hello' (null Dat))))
  (println (get a c))
)