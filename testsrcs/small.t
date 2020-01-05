(data Dat 
 (
   a:int
   b:bool
   c:string
   d:Dat
 )
)

(
  def f (a : int) (b : string) : string
  (
    if (= a 1)
    b
    (f (dec a) (println (+ b 'add')))
  )
)

(let ((b:int 5)) 
  (println (f b (println 'mead')) ) 
  (let (( c:Dat (null Dat) )) 
    (set c (Dat 10 true 'for dat' c ))
    (println (get c c))
    (println 'nestedlet')
  )
  (println 'im done boys')
)
