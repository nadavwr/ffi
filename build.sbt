lazy val commonSettings = Def.settings(
  scalaVersion := "2.11.11",
  organization := "com.github.nadavwr",
  publishArtifact in (Compile, packageDoc) := false,
  licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
)

lazy val unpublished = Def.settings(
  publish := {},
  publishLocal := {},
  publishM2 := {}
)

lazy val `libffi-scala-native` = project
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings)

lazy val `libffi-scala-native-test` = project
  .enablePlugins(ScalaNativePlugin)
  .settings(
    commonSettings,
    unpublished,
    resolvers += Resolver.bintrayRepo("nadavwr", "maven"),
    libraryDependencies += "com.github.nadavwr" %%% "makeshift" % "0.1.3",
    test := run.toTask("").value
  )
  .dependsOn(`libffi-scala-native`)

lazy val `libffi-scala-native-root` = (project in file("."))
  .aggregate(`libffi-scala-native`, `libffi-scala-native-test`)
  .settings(
    commonSettings,
    unpublished,
    test := { (test in `libffi-scala-native-test`).value },
    publish := { (publish in `libffi-scala-native`).value },
    publishLocal := { (publishLocal in `libffi-scala-native`).value },
    clean := { (clean in `libffi-scala-native`).value; (clean in `libffi-scala-native-test`).value }
  )

