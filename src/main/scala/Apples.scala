case class Apple(val color: String)

def generateApple(): Apple = {
  val rand = new scala.util.Random
  if (rand.nextBoolean())
    Apple("Red")
  else
    null
}

def generateAppleOption(): Option[Apple] = {
  val rand = new scala.util.Random
  if (rand.nextBoolean())
    Some(Apple("Red"))
  else
    None
}

def nullSafe() = {
  var myApple: Apple = null;
  myApple = generateApple()

  if (myApple != null)
    println(myApple.color)
}

def unsafe() = {
  var myApple: Apple = null;
  myApple = generateApple()

  println(myApple.color)
}

def optionPattern() = {
  var myAppleOption: Option[Apple] = None;
  myAppleOption = generateAppleOption()
  //myAppleOption can either be None or Some(apple)

  println(
    myAppleOption match
    case Some(myApple) => myApple.color
    case None => "hello"
  )
}
@main
def main() = {
  nullSafe()
  optionPattern()
  //unsafe()
}
