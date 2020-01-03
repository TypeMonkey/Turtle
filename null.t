(
  def f (a : int) (b : string) : string
  (
    if (= a 3)
    b
    (f (dec a) (println (+ b 'add')))
  )
)

(let ((b:int 5)) 
  (println (f b (println 'mead')) )
  (println 'hello world')
  (set b 
   (if true
      10
      5
    ) 
  )
  (println (toStr b))
)
