<img src="https://raw.githubusercontent.com/TypeMonkey/Turtle/master/imgs/turtle.png" width="450" height="250">

# Turtle 
_A statically typed , esoteric ML that uses long-term storage for memory use _


### Features
_"Sometimes, you just gotta take things slow"_

Turtle builds directly upon the syntax and semantics of [sNEK](https://github.com/TypeMonkey/sNEK), which is another ML I made back in September 2019. The main addition being custom datatypes and using long-term storage for memory.

### Background
_"That's a lot of bytes"_

'Twas the season of Christmas. I had just finished a stressful Fall Quarter and practically did nothing for week, but sleep and eat. Soon enough, I realized that Christmas was near and I didn't even think about buying presents.

While shopping online, I slowly built an interest in how distributions managed deliveries for optimal speed. Soon enough, my tabs for "Child-safe Lego blocks" shrunk to accomadate tabs for "Average UPS speed", "Amazon distributions strategies", etc.

I got lost in the sauce. I knew I would most likely never apply this information in my life, yet it was so interesting. My Googlefu started to lead me to articles that intesect distribution strategies with computer architecture. Blog posts about comparisons between road design and computer busses soon popped up.

Then, I came upon this [image](https://www.formulusblack.com/wp-content/uploads/2019/02/Screen-Shot-2019-02-01-at-12.16.39-PM.png)

While others may see just a chart, I saw an ... _opportunity_. In today's modern era, we are making leaps and bounds in data storage efficiency and speed. While only a few decades ago, one would measure computation in days, we now have the luxury of worrying over mere microseconds. 

But to an average human, is there any difference? Can one naturally perceive a latency, in the matter of microseconds, to display annoyance?

A common complaint that many in the software industry share is the intense and fast paced culture of the workplace. It seems like everyday, a new thing is invented and one is expected to master it the next day. 

Sometimes, it's just nice to sit down, crack open a cold bottle of Sprite Cranberry and watch as your program slowly executes itself just a couple miliseconds more while your machine's rising temperature provides that Holiday comfort.


### Requirements
Turtle is written in Java 1.8 and depends on a modified version of Grammatica 1.6 for its grammar and parsing. Apache Commons CLI 1.4 is also used for command line argument parsing. 

Every binary will be released with Grammatica 1.6 and Apache Common CLI 1.4 included, so your machine needs to only have Java 8 for sNEK to run.

### Writing in Turtle
The following link: [wiki](https://github.com/TypeMonkey/sNEK/wiki/Syntax-of-sNEK) goes over the basic 8 syntax of Turtle. 

Unlike sNEK, Turtle allows for the creation and manipulation of custom datatypes. 

For example, if we'd like to declare a custom datatype that holds an `int` , `string` and `bool`
```
( data Holder
  (
    a : int
    b : string
    c : bool
  )
)
```

To instanstiate `Holder` , we simply type `(Holder 10 true 'hello world')`

Custom datatypes are immutable. Say we had the following code:
```
( let ((hold:Holder (Holder 10 true 'hello world') ))
   ...
)
```

To change the value of `a` in our instance of Holder, we type:
```
( let ((hold:Holder (Holder 10 true 'hello world') ))
   (mut hold a 50)
)
```

To retrieve the value of 'b' in our instance of Holder, we type:
```
( let ((hold:Holder (Holder 10 true 'hello world') ))
   (get hold b)
)
```

Turtle allows for null values. However, null values must be provided with their
type information.
```
( let ((hold:Holder (Holder 10 true 'hello world') ))
   (set hold (null Holder))
)
```

### Credits and Thanks
I'd like to thank Professor Brett Stalbaum of the Visual Arts Department at UCSD for making me realize that software art and software engineering aren't opposites. 

Also, I'd like to thank the Bob Nystrom and his online book [_Crafting Interpreters_](https://craftinginterpreters.com/) for kickstarting my many PL projects.
