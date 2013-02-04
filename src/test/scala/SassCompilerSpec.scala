import org.scalatest.FunSpec
import java.io.File
import net.litola.SassCompiler

class SassCompilerSpec extends FunSpec {
  describe("SassCompiler") {
    it("should compile well-formed scss file") {
      val scssFile = new File("src/test/resources/ok.scss")
      val (full, minified, deps) = SassCompiler.compile(scssFile, Nil)
      assert(full.replaceAll("""/\* line.* \*/\n""", "") === ".test {\n  display: none; }\n")
      assert(minified.orNull === ".test{display:none}\n")
      assert(deps.length === 1)
      assert(deps(0).getName() === "ok.scss")
    }
    it("should compile well-formed scss file containing import") {
      val scssFile = new File("src/test/resources/ok_import.scss")
      val (full, minified, deps) = SassCompiler.compile(scssFile, Nil)
      assert(full.replaceAll("""/\* line.* \*/\n""", "") === ".test-import {\n  color: black; }\n\n.test {\n  display: none; }\n")
      assert(minified.orNull === ".test-import{color:black}.test{display:none}\n")
      assert(deps.length === 2)
      assert(deps(0).getName() === "_imported.scss")
      assert(deps(1).getName() === "ok_import.scss")
    }
    it("should fail to compile malformed scss file") {
      val scssFile = new File("src/test/resources/broken.scss")
      val thrown = intercept[sbt.PlayExceptions.AssetCompilationException] {
        SassCompiler.compile(scssFile, Nil)
      }
      val expectedMessage =
        """Compilation error[Sass compiler: Syntax error: Invalid CSS after "	display: none;": expected "}", was ""]"""
      assert(thrown.line === 3)
      assert(thrown.getMessage === expectedMessage)
    }
  }
}
