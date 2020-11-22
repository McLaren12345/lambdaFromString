package pl.joegreen.lambdaFromString;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.junit.jupiter.api.Test;
import pl.joegreen.lambdaFromString.classFactory.ClassFactory;
import pl.joegreen.lambdaFromString.classFactory.DefaultClassFactory;

import javax.tools.JavaCompiler;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaFactoryConfigurationTest {

	@Test
	public void addingImportsAndStaticImportsWithMultipleSteps() {
		List<String> imports = Arrays.asList("i1", "i2", "i3");
		List<String> staticImports = Arrays.asList("si1", "si2", "si3");

		LambdaFactoryConfiguration conf = LambdaFactoryConfiguration.get()
				.withStaticImports(staticImports.get(0))
				.withImports(imports.get(0), imports.get(1))
				.withImports(imports.get(2))
				.withStaticImports(staticImports.get(1), staticImports.get(2));
		assertEquals(imports, conf.getImports());
		assertEquals(staticImports, conf.getStaticImports());
	}

	@Test
	public void addingImportsCreatesNewConfiguration() {
		LambdaFactoryConfiguration defaultConf = LambdaFactoryConfiguration.get();
		LambdaFactoryConfiguration withImport = defaultConf.withImports("abc");
		LambdaFactoryConfiguration withStaticImport = defaultConf.withStaticImports("def");
		assertNotEquals(defaultConf, withImport);
		assertNotEquals(defaultConf, withStaticImport);
	}

	@Test
	public void usingWithSetsParameters() {
		HelperClassSourceProvider helper = new DefaultHelperClassSourceProvider();
		ClassFactory classFactory = new DefaultClassFactory();
		String[] staticImports = new String[] { "si1", "si2" };
		String[] imports = new String[] { "i1", "i2" };
		String compilationClassPath = "compilationClassPath";
		ClassLoader parentClassLoader = new URLClassLoader(new URL[] {});
		JavaCompiler javaCompiler = new EclipseCompiler();

		LambdaFactoryConfiguration changedConfiguration = LambdaFactoryConfiguration.get()
				.withHelperClassSourceProvider(helper)
				.withClassFactory(classFactory)
				.withStaticImports(staticImports)
				.withImports(imports)
				.withCompilationClassPath(compilationClassPath)
				.withParentClassLoader(parentClassLoader)
				.withJavaCompiler(javaCompiler)
				.withEnablePreview(true)
				.withJavaVersion(17);

		assertSame(helper, changedConfiguration.getDefaultHelperClassSourceProvider());
		assertSame(classFactory, changedConfiguration.getClassFactory());
		assertEquals(Arrays.asList(staticImports), changedConfiguration.getStaticImports());
		assertEquals(Arrays.asList(imports), changedConfiguration.getImports());
		assertEquals(compilationClassPath, changedConfiguration.getCompilationClassPath());
		assertSame(parentClassLoader, changedConfiguration.getParentClassLoader());
		assertSame(javaCompiler, changedConfiguration.getJavaCompiler());
		assertTrue(changedConfiguration.getEnablePreview());
		assertEquals(17, changedConfiguration.getJavaVersion());
	}

}
