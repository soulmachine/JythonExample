package me.soulmachine;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * A simple Jython example to execute Python scripts from Java.
 */
final class JythonExample {
  private JythonExample() {}

  /**
   * Main entrypoint.
   *
   * @param args arguments
   * @throws ScriptException ScriptException
   */
  public static void main(final String[] args) throws ScriptException {
    listEngines();

    // Integrate with ScriptEngine
    {
      final ScriptEngineManager mgr = new ScriptEngineManager();
      final ScriptEngine pyEngine = mgr.getEngineByName("python");

      try {
        pyEngine.eval("print \"Python - Hello, world!\"");
      } catch (ScriptException ex) {
        ex.printStackTrace();
      }
    }

    final PythonInterpreter interpreter = new PythonInterpreter();
    interpreter.exec("print \"Python - Hello, world!\"");

    PyObject result = interpreter.eval("2 + 3");
    System.out.println(result.toString());

    // Define a function and call it from Java
    interpreter.exec("def myLowerCase(s):\n\treturn s.lower()\n");
    final PyObject myLowerCase = interpreter.get("myLowerCase");
    result = myLowerCase.__call__(new PyString("TEST!"));
    final String realResult = (String)result.__tojava__(String.class);
    System.out.println(realResult);
  }

  /**
   * Display all script engines.
   */
  public static void listEngines() {
    final ScriptEngineManager mgr = new ScriptEngineManager();
    final List<ScriptEngineFactory> factories =
        mgr.getEngineFactories();
    for (final ScriptEngineFactory factory: factories) {
      System.out.println("ScriptEngineFactory Info");

      final String engName = factory.getEngineName();
      final String engVersion = factory.getEngineVersion();
      final String langName = factory.getLanguageName();
      final String langVersion = factory.getLanguageVersion();

      System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);

      final List<String> engNames = factory.getNames();
      for (final String name: engNames) {
        System.out.printf("\tEngine Alias: %s\n", name);
      }
      System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
    }
  }
}
