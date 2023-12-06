# Is Option[Apple] the same as if(apple != null)?

## Unsafe access of nullable variables

Consider this code:
```scala
case class Apple(val color: String)

@main
def main() = {
    var myApple = null;

    def generateApple(): Apple = {
        val rand = new scala.util.Random
        if (rand.next() > 0.5)
            Apple("Red")
        else
            null
    }
    myApple = generateApple()

    println(myApple.color)
}

```
This code works half of the time. Sometimes apple is null, and null.color is not a valid access, since null has no color-field. So it crashes.
```
Exception in thread "main" java.lang.NullPointerException
	at Colours$package$.main(Colours.scala:20)
	at main.main(Colours.scala:4)

Process finished with exit code 1
```

Easy to fix though! Just add a null check, right?

```scala
case class Apple(val color: String)

@main
def main() = {
  var myApple = null;

  def generateApple(): Apple = {
    val rand = new scala.util.Random
    if (rand.next() > 0.5)
      Apple("Red")
    else
      null
  }

  myApple = generateApple()

  if (myApple != null)
    println(myApple.color)
}
```
Now it only prints, if apple is not null, task accomplished, well done.  
Well, yes it does work now. But who can guarantee that you will ALWAYS think to do a null check before using apple?  
The compiler will not force you, and noone will stop you to cause a nullpointer by accessing null.color.  
Did you know that in java every Object can always be null, and in theory you would have to null check EVERY SINGLE TIME you used ANY object?  
Instead, we usually just trust that its not null and hope really hard, that we were correct in that assumption.  
Our project now runs on "In God We Trust"  

## Option[Apple] 

This is exactly why the Option Pattern exists. Its basically a nullcheck, but the compiler will notice if you FORGET the null check and 
will not compile until you do:

```scala
case class Apple(val color: String)

def generateAppleOption(): Option[Apple] = {
  val rand = new scala.util.Random
  if (rand.nextBoolean())
    Some(Apple("Red"))
  else
    None
}

@main
def main() = {
  var myAppleOption: Option[Apple] = None;
  myAppleOption = generateAppleOption()
  //myAppleOption can either be None or Some(apple)

  println(
    myAppleOption match {
      case Some(myApple) => myApple.color
      case None => "hello"  //try deleting the line, it will not compile anymore
    }
  )
}
```

"But that code is so much longer and its annoying and i dont wanna use it :("  
Yes, it is more verbose and it can be annoying. But you will notice, that the compiler will force you always handle both possible cases:
- Apple is not None => myAppleOption has value Some(Apple("Red"))  
- Apple is None => myAppleOption has value None  
It will not allow you to compile until you handle both cases.  
So you can NEVER produce a nullpointer on accident anymore, and have eliminated 85% of all the errors you ever encounter in java.  
  
You trade safety for verbose code. (opinion)  
That is a very common tradeoff:   
JavaScript has no static type system and will not stop you if you try to access null.undefined.apple.banana until that line is executed (and crashes)  
Scala will not even allow you to compile the project.

## Producing "nullpointers" with Option
If you try really hard, you can create a "Nullpointer" with Option too, by tricking the compiler:
But you really have to try hard in this case, it wont happen on accident.
```scala
case class Apple(val color: String)

def generateAppleOption(): Option[Apple] = {
  val rand = new scala.util.Random
  if (rand.nextBoolean())
    Some(Apple("Red"))
  else
    None
}

@main
def main() = {
  var myAppleOption: Option[Apple] = None;
  myAppleOption = generateAppleOption()
  //myAppleOption can either be None or Some(apple)

  println(myAppleOption.get.color)  //will throw exception half of the time
}
```
the above code will unwrap myAppleOption, and will just assume that its Some(Apple) and not None.
Thats equivalent to just accessing apple.color, ignoring that it could be null.  
This code will crash half the time, if myAppleOption = None:
```
Exception in thread "main" java.util.NoSuchElementException: None.get
	at scala.None$.get(Option.scala:627)
	at scala.None$.get(Option.scala:626)
	at Apples$package$.optionPattern(Apples.scala:40)
	at Apples$package$.main(Apples.scala:46)
	at main.main(Apples.scala:43)
```
Just dont use it. Its unsafe and completely kills the advantage of using option.
Now you have verbose Option code thats unsafe to use, the worst of both worlds :D  
Stick to pattern matching.  

## Conclusion
Option can be seen as a forced nullcheck for your object.
It makes the compiler remind/force you to handle all cases, so you never get an unexpected value for "apple" if you try to access apple.color
Pattern matching will allow us to unwrap the Option[Apple] and handle both cases:
- apple is there: Some(apple)
- apple is not there: None
