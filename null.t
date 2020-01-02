(
  def f (a : int) (b : string) : string
  (
    if (= a 3)
    b
    (f (- a 1) (println (+ b 'add')))
  )
)

(let ((b:int 5)) 
  (println (f b (println 'mead')) )
)
