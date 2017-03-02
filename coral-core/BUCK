# findbugs
remote_file(
  name = "jsr305.jar",
  url  = "mvn:com.google.code.findbugs:jsr305:jar:3.0.1",
  sha1 = "f7be08ec23c21485b9b5a1cf1654c2ec8c58168d"
)

prebuilt_jar(
  name = "jsr305",
  binary_jar = ":jsr305.jar"
)

java_library(
  name = "lib",
  srcs = glob(["src/main/java/**/*.java"]),
  deps = [":jsr305"],
  visibility = ['PUBLIC']
)
