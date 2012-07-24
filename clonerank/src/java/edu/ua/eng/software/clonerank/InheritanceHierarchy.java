import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Scope;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import com.sun.tools.javac.tree.JCTree;

class UnreachableException extends RuntimeException
{
    public UnreachableException(String msg) {
        super(msg);
    }
}

public class InheritanceHierarchy
{
    public static void main(String[] args) throws IOException, FileNotFoundException {
        List<String> options = new ArrayList<String>();
        options.add("-g"); // ensure every file has a LineMap
        List<File> files = new ArrayList<File>();
        File argfile = null;
        for (String arg : args) {
            if (arg.startsWith("@")) {
                argfile = new File(arg.substring(1));
            } else if (arg.endsWith(".java")) {
                files.add(new File(arg));
            } else {
                if (!arg.startsWith("-g")) { // get rid of potential "-g:none", etc.
                    options.add(arg);
                }
            }
        }
        if (null != argfile) {
            FileInputStream fis = new FileInputStream(argfile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String filename = br.readLine();
            while (null != filename) {
                files.add(new File(filename));
                filename = br.readLine();
            }

            br.close();
            isr.close();
            fis.close();
        }

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        final Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromFiles(files);
        final JavacTask task = (JavacTask)compiler.getTask(null, fileManager, null, options, null, fileObjects);

        final Iterable<? extends CompilationUnitTree> asts = task.parse();
        final Iterable<? extends Element> asgs = task.analyze();

        Elements elements = task.getElements();
        Types types = task.getTypes();
        Trees trees = Trees.instance(task);

        for (CompilationUnitTree ast : asts) {
            InheritanceHierarchyVisitor v = new InheritanceHierarchyVisitor(ast, trees, elements, types);
            try {
                v.scan(ast, null);
            } catch (UnreachableException e) {
                //System.err.println(e);
                e.printStackTrace();
                System.err.flush();
                System.out.flush();
            }
        }
    }

    static class InheritanceHierarchyVisitor extends TreePathScanner<Void,Void>
    {
        private CompilationUnitTree cut;
        private Trees trees;
        private Elements elements;
        private Types types;
        private SourcePositions sourcePositions;
        private LineMap lineMap;

        public InheritanceHierarchyVisitor(CompilationUnitTree cut, Trees trees, Elements elements, Types types) {
            this.cut = cut;
            this.trees = trees;
            this.elements = elements;
            this.types = types;

            this.sourcePositions = trees.getSourcePositions();
            this.lineMap = cut.getLineMap();
        }

        @Override
        public Void visitClass(ClassTree node, Void ignored) {
            super.visitClass(node, ignored);

            long lineno = lineMap.getLineNumber(sourcePositions.getStartPosition(cut, node));

            TreePath path = TreePath.getPath(cut, node);

            Tree extendsClause = node.getExtendsClause();
            if (null == extendsClause) {
                    System.out.println("Null extendsClause");
                    return null;
            }
            Tree.Kind kind = extendsClause.getKind();
            System.out.println("Tree.Kind is " + kind);
            if (kind == Tree.Kind.IDENTIFIER) {
                String name = ((ClassTree) extendsClause).getSimpleName().toString();
                System.out.println("--> " + name);
            } else if (kind == Tree.Kind.MEMBER_SELECT){
                String name = ((
            } else {
                throw new UnreachableException("visitMethodInvocation: kind == " + kind);
            }

            return null;
        }

    }
}
