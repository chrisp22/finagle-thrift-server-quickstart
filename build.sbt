name := "finagle-thrift-server-quickstart"

version := "0.1.0"

com.twitter.scrooge.ScroogeSBT.newSettings

scalariformSettings

resolvers ++= Seq(
  "twttr" at "http://maven.twttr.com/"
)

val finagleVersion = "6.33.0"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-core" % finagleVersion,
  "com.twitter" %% "finagle-thrift" % finagleVersion,
  "com.twitter" %% "finagle-thriftmux" % finagleVersion,
  "com.twitter" %% "scrooge-core" % "4.5.0",
  "org.apache.thrift" % "libthrift" % "0.9.3",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
