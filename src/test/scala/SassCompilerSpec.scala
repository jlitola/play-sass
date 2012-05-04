import org.scalatest.Spec
import java.io.File
import net.litola.SassCompiler

class SassCompilerSpec extends Spec {
  describe("SassCompiler") {
    it("should compile well-formed scss file") {
      val scssFile = new File("src/test/resources/ok.scss")
      val (full, minified, file) = SassCompiler.compile(scssFile, Nil)
      assert(full === ".test {\n  display: none; }\n")
      assert(minified.orNull === ".test{display:none}\n")
    }
    it("should compile well-formed scss file with import") {
      val scssFile = new File("src/test/resources/ok_import.scss")
      val (full, minified, file) = SassCompiler.compile(scssFile, Nil)
      assert(full === ".test-import {\n  color: black; }\n\n.test {\n  display: none; }\n")
      assert(minified.orNull === ".test-import{color:black}.test{display:none}\n")
    }
    it("should fail to compile malformed scss file") {
      val scssFile = new File("src/test/resources/broken.scss")
      val thrown = intercept[sbt.PlayExceptions.AssetCompilationException] {
        SassCompiler.compile(scssFile, Nil)
      }
      val expectedMessage =
        """Compilation error [Sass compiler: Syntax error: Invalid CSS after "	display: none;": expected "}", was ""]"""
      assert(thrown.line.getOrElse(0) === 3)
      assert(thrown.getMessage === expectedMessage)
    }
  }
}
