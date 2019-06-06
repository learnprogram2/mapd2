package cn.gasin.mine.processer;

import cn.gasin.mine.inter.MyAnno;
import com.sun.deploy.util.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

/**
 * @author wongyiming
 * @date 2019/6/6 17:05
 */
@SupportedAnnotationTypes("cn.gasin.mine.inter.MyAnno")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcesser extends AbstractProcessor {

	/**
	 * 类名的后缀
	 */
	public static final String SUFFIX = "AutoGenerate";

	/**
	 * {@inheritDoc}
	 *
	 * @param annotations
	 * @param roundEnv
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();

		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				//打印:
				messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + element.toString());
				messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + element.getSimpleName());
				messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + element.getEnclosedElements());

				//1. 拿到element的注解, 拿到
				MyAnno elementAnnotation = element.getAnnotation(MyAnno.class);
				String simpleName        = element.getSimpleName().toString();
				simpleName = Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1);
				Element enclosingElement = element.getEnclosingElement();
				String  enclosingQualifiedName;
				if (enclosingElement instanceof PackageElement) {
					enclosingQualifiedName = ((PackageElement) enclosingElement).getQualifiedName().toString();
				} else {
					enclosingQualifiedName = ((TypeElement) enclosingElement).getQualifiedName().toString();
				}

				//2. 生成文件
				try {
					//生成包名, 类名
					String packageName = enclosingQualifiedName.substring(0, enclosingQualifiedName.lastIndexOf("."));
					simpleName = simpleName + SUFFIX;
					//创建文件
					JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(simpleName);
					messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + sourceFile.toUri());
					//写文件
					Writer writer = sourceFile.openWriter();
					try {
						PrintWriter printWriter = new PrintWriter(writer);
						printWriter.println("package" + packageName + ";");
						printWriter.println("\npublic class" + simpleName + " {");
						printWriter.println("\n ");
						printWriter.println("   public static void print" + simpleName + "{");
						printWriter.println("    //注解的父元素:" + enclosingElement.toString());
						printWriter.println("    System.out.println(\"代码生成的路径是" + sourceFile.toUri() + "\");");
						printWriter.println("    System.out.println(\"注解的元素:" + element.toString() + "\");");
						printWriter.println("    System.out.println(\"注解的版本:" + annotation + "\");");
						printWriter.println("   }");
						printWriter.println("}");
						printWriter.flush();
					} finally {
						writer.close();
					}
				} catch (Exception e) {
					messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
				}
			}
		}
		return true;
	}
}
