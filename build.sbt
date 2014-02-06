name := "bitcargo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "com.typesafe" %% "play-plugins-mailer" % "2.1.0"
)

play.Project.playJavaSettings
