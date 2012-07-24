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

public class Hack
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
            HackVisitor v = new HackVisitor(ast, trees, elements, types);
            try {
                boolean scan = true;
                for (Tree tree : ast.getTypeDecls()) {
                    ClassTree classTree = (ClassTree)tree;
                    if (0 != classTree.getTypeParameters().size()) {
                        scan = false;
                    }
                }
                if (scan) {
                    v.scan(ast, null);
                }
            } catch (UnreachableException e) {
                //System.err.println(e);
                e.printStackTrace();
                System.err.flush();
                System.out.flush();
            }
        }
    }

    static class HackVisitor extends TreePathScanner<Void,Void>
    {
        private CompilationUnitTree cut;
        private Trees trees;
        private Elements elements;
        private Types types;
        private SourcePositions sourcePositions;
        private LineMap lineMap;

        public HackVisitor(CompilationUnitTree cut, Trees trees, Elements elements, Types types) {
            this.cut = cut;
            this.trees = trees;
            this.elements = elements;
            this.types = types;

            this.sourcePositions = trees.getSourcePositions();
            this.lineMap = cut.getLineMap();
        }

        private String caller(TreePath path) {
            TreePath callerPath = path.getParentPath();
            while ((null != callerPath) && (Tree.Kind.METHOD != callerPath.getLeaf().getKind())) {
                callerPath = callerPath.getParentPath();
            }
            if (null == callerPath) {
                return "<CLASS_BODY>";
            }
            MethodTree caller = (MethodTree)(callerPath.getLeaf());
            ExecutableElement callerElement = (ExecutableElement)(trees.getElement(callerPath));
            String callerName = callerElement.toString();
            Scope callerScope = trees.getScope(callerPath);
            TypeElement callerClass = callerScope.getEnclosingClass();
            String callerClassName = callerClass.getQualifiedName().toString();
            return (callerClassName + "." + callerName);
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, Void ignored) {
            super.visitMethodInvocation(node, ignored);

            long lineno = lineMap.getLineNumber(sourcePositions.getStartPosition(cut, node));

            TreePath path = TreePath.getPath(cut, node);

            ExpressionTree methodSelect = node.getMethodSelect();
            Tree.Kind kind = methodSelect.getKind();
            if (kind == Tree.Kind.MEMBER_SELECT) {
                MemberSelectTree memberSelect = (MemberSelectTree)methodSelect;
                ExpressionTree receiver = memberSelect.getExpression();
                Name receiverName = memberSelect.getIdentifier();
                TypeMirror receiverType = ((JCTree)receiver).type;
                if (TypeKind.ARRAY == receiverType.getKind()) {
                    System.out.println("No element for Array type: " + receiverType);
                    return null;
                }
                TypeElement receiverElement = (TypeElement)types.asElement(receiverType);
                List<? extends Element> receiverMembers = elements.getAllMembers(receiverElement);
                List<ExecutableElement> receiverMethods = ElementFilter.methodsIn(receiverMembers);
                List<ExecutableElement> candidates = new ArrayList<ExecutableElement>();
                for (ExecutableElement method : receiverMethods) {
                    if (method.getSimpleName().toString().equals(receiverName.toString())) {
                        candidates.add(method);
                    }
                }
                if (0 == candidates.size()) {
                    throw new UnreachableException("Failed to find matching method for " + receiverName.toString());
                } else {
                    String caller = caller(path);
                    if (!"<CLASS_BODY>".equals(caller)) {
                        //System.out.println("Caller: " + caller);
                        String callee = callee(node, candidates, node.getArguments());
                        //System.out.println("\tCallee: " + receiverElement.getQualifiedName() + "." + callee + "\n");
                    }
                }
            } else if (kind == Tree.Kind.IDENTIFIER) {
                String name = ((IdentifierTree)methodSelect).getName().toString();

                if ("super".equals(name)) {
                    Scope scope = trees.getScope(path);
                    if (null == scope) {
                        System.out.println("(super) Scope not available for " + name);
                    } else {
                        TypeElement clazz = scope.getEnclosingClass();
                        TypeMirror superClass = clazz.getSuperclass();
                        TypeElement superElement = (TypeElement)types.asElement(superClass);
                        List<? extends Element> superMembers = elements.getAllMembers(superElement);
                        List<ExecutableElement> superInits = ElementFilter.constructorsIn(superMembers);
                        List<ExecutableElement> candidates = new ArrayList<ExecutableElement>();
                        for (ExecutableElement init : superInits) {
                            if (init.getSimpleName().toString().equals("<init>")) {
                                candidates.add(init);
                            } else {
                                throw new UnreachableException("Constructor name != \"<init>\" (" + init.getSimpleName().toString() + ")");
                            }
                        }
                        if (0 == candidates.size()) {
                            throw new UnreachableException("(super) Failed to find matching constructor for " + name);
                        } else {
                            String caller = caller(path);
                            if (!"<CLASS_BODY>".equals(caller)) {
                                //System.out.println("Caller: " + caller);
                                String callee = callee(node, candidates, node.getArguments());
                                //System.out.println("\tCallee: " + superElement.getQualifiedName() + "." + callee + "\n");
                            }
                        }
                    }

                    return null;
                }

                if ("this".equals(name)) {
                    Scope scope = trees.getScope(path);
                    if (null == scope) {
                        System.out.println("(this) Scope not available for " + name);
                    } else {
                        TypeElement classElement = scope.getEnclosingClass();
                        List<? extends Element> classMembers = elements.getAllMembers(classElement);
                        List<ExecutableElement> classInits = ElementFilter.constructorsIn(classMembers);
                        List<ExecutableElement> candidates = new ArrayList<ExecutableElement>();
                        for (ExecutableElement init : classInits) {
                            if (init.getSimpleName().toString().equals("<init>")) {
                                candidates.add(init);
                            } else {
                                throw new UnreachableException("Constructor name != \"<init>\" (" + init.getSimpleName().toString() + ")");
                            }
                        }
                        if (0 == candidates.size()) {
                            throw new UnreachableException("(this) Failed to find matching constructor for " + name);
                        } else {
                            String caller = caller(path);
                            if (!"<CLASS_BODY>".equals(caller)) {
                                //System.out.println("Caller: " + caller);
                                String callee = callee(node, candidates, node.getArguments());
                                //System.out.println("\tCallee: " + classElement.getQualifiedName() + "." + callee + "\n");
                            }
                        }
                    }

                    return null;
                }

                Scope scope = null;
                try {
                    scope = trees.getScope(path);
                } catch (AssertionError e) {
                    System.out.println("SCOPE CRASH: " + cut.getSourceFile().getName());
                    System.out.println("SCOPE CRASH: " + node);
                    Element element = trees.getElement(path);
                    System.out.println("getElement Succeeded");
                }
                if (null == scope) {
                    throw new UnreachableException("Scope not available for " + name);
                } else {
                    TypeElement clazz = scope.getEnclosingClass();
                    List<ExecutableElement> candidates = new ArrayList<ExecutableElement>();
                    addCandidates(name, clazz, candidates);
                    if (0 == candidates.size()) {
                        // Could be in a nested (possibly anonymous) class --- need to check for that above
                        Element enclosing = clazz.getEnclosingElement();
                        Element walk = enclosing;
                        while (null != walk) {
                            if (ElementKind.CLASS == walk.getKind()) {
                                enclosing = walk;
                                clazz = (TypeElement)enclosing;
                                addCandidates(name, clazz, candidates);
                                if (0 != candidates.size()) {
                                    break;
                                } else {
                                    clazz = (TypeElement)types.asElement(clazz.getSuperclass());
                                    addCandidates(name, clazz, candidates);
                                    if (0 != candidates.size()) {
                                        break;
                                    }
                                }
                            }
                            walk = walk.getEnclosingElement();
                        }
                    }
                    if (0 == candidates.size()) {
                        debug("SNC", node, name, caller(path), lineno);
                        throw new UnreachableException("Still no candidates");
                    } else {
                        String caller = caller(path);
                        if (!"<CLASS_BODY>".equals(caller)) {
                            //System.out.println("Caller: " + caller);
                            String callee = callee(node, candidates, node.getArguments());
                            //System.out.println("\tCallee: " + clazz.getQualifiedName() + "." + callee + "\n");
                        }
                    }
                }

                return null;
            } else {
                throw new UnreachableException("visitMethodInvocation: kind == " + kind);
            }

            return null;
        }

        private void debug(String prefix, Tree node, String name, String caller, long lineno) {
            System.out.flush();
            System.out.println(prefix + " node: " + node);
            System.out.println(prefix + " name: " + name);
            System.out.println(prefix + " file: " + cut.getSourceFile().getName());
            System.out.println(prefix + " line: " + lineno);
            System.out.println(prefix + " caller: " + caller);
            System.out.flush();
        }

        private List<ExecutableElement> addCandidates(String name, TypeElement clazz, List<ExecutableElement> candidates) {
            List<? extends Element> clazzMembers = elements.getAllMembers(clazz);
            List<ExecutableElement> clazzMethods = ElementFilter.methodsIn(clazzMembers);
            for (ExecutableElement method : clazzMethods) {
                if (name.equals(method.getSimpleName().toString())) {
                    candidates.add(method);
                }
            }
            return candidates;
        }

        private boolean isSameBoxedType(TypeMirror lhs, TypeMirror rhs) {
            boolean same = true;
            TypeKind lhsKind = lhs.getKind();
            TypeKind rhsKind = rhs.getKind();
            boolean isPrimitiveLhs = lhsKind.isPrimitive();
            boolean isPrimitiveRhs = rhsKind.isPrimitive();
            if ((isPrimitiveLhs) && (!isPrimitiveRhs)) {
                TypeElement boxedLhs = types.boxedClass((PrimitiveType)lhs);
                if (!types.isSameType(boxedLhs.asType(), rhs)) {
                    same = false;
                }
            } else if ((!isPrimitiveLhs) && (isPrimitiveRhs)) {
                TypeElement boxedRhs = types.boxedClass((PrimitiveType)rhs);
                if (!types.isSameType(lhs, boxedRhs.asType())) {
                    same = false;
                }
            } else {
                same = false;
            }
            return same;
        }

        private boolean isSameErasureType(TypeMirror lhs, TypeMirror rhs) {
            boolean same = true;
            if (!types.isSameType(types.erasure(lhs), types.erasure(rhs))) {
                same = false;
            }
            return same;
        }

        // Returns true if and only if lhs is a direct supertype of rhs
        private boolean isDirectSupertype(TypeMirror lhs, TypeMirror rhs) {
            boolean direct = false;
            TypeMirror lhsErasure = types.erasure(lhs);
            TypeMirror rhsErasure = types.erasure(rhs);
            List<? extends TypeMirror> supertypes = types.directSupertypes(rhsErasure);
            for (TypeMirror supertype : supertypes) {
                TypeMirror supertypeErasure = types.erasure(supertype);
                if (types.isSameType(supertypeErasure, lhsErasure)) {
                    direct = true;
                    break;
                }
            }
            return direct;
        }


        private boolean exactFixedArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                if (!types.isSameType(argTypes.get(i), paramTypes.get(i))) {
                    match = false;
                    break;
                }
            }
            return match;
        }

        private boolean boxingFixedArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argType = argTypes.get(i);
                TypeMirror paramType = paramTypes.get(i);
                if (types.isSameType(argType, paramType)) continue;
                if (!isSameBoxedType(argType, paramType)) {
                    match = false;
                    break;
                }
            }
            return match;
        }

        private boolean assignableFixedArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                if (!types.isAssignable(argTypes.get(i), paramTypes.get(i))) {
                    match = false;
                    break;
                }
            }
            return match;
        }

        private boolean assignableErasureFixedArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argErasure = types.erasure(argTypes.get(i));
                TypeMirror paramErasure = types.erasure(paramTypes.get(i));
                if (!types.isAssignable(argErasure, paramErasure)) {
                    match = false;
                    break;
                }
            }
            return match;
        }

        private boolean exactVarArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int numParamTypes = paramTypes.size();
            int numArgTypes = argTypes.size();
            if (numArgTypes < numParamTypes) {
                // Ignore the last (varArgs) parameter
                for (int i = 0; i < numArgTypes; ++i) {
                    if (!types.isSameType(argTypes.get(i), paramTypes.get(i))) {
                        match = false;
                        break;
                    }
                }
            } else {
                // Check the first (numParamTypes - 1) argTypes against their corresponding paramTypes
                for (int i = 0; i < (numParamTypes - 1); ++i) {
                    if (!types.isSameType(argTypes.get(i), paramTypes.get(i))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    // Check the remaining argTypes against the last (varArgs) paramType
                    TypeMirror varArgsParamType = ((ArrayType)paramTypes.get(numParamTypes - 1)).getComponentType();
                    for (int i = (numParamTypes - 1); i < numArgTypes; ++i) {
                        if (!types.isSameType(argTypes.get(i), varArgsParamType)) {
                            match = false;
                            break;
                        }
                    }
                }
            }
            return match;
        }

        private boolean boxingVarArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int numParamTypes = paramTypes.size();
            int numArgTypes = argTypes.size();
            if (numArgTypes < numParamTypes) {
                // Ignore the last (varArgs) parameter
                for (int i = 0; i < numArgTypes; ++i) {
                    TypeMirror paramType = paramTypes.get(i);
                    TypeMirror argType = argTypes.get(i);
                    if (types.isSameType(argType, paramType)) continue;
                    if (!isSameBoxedType(argType, paramType)) {
                        match = false;
                        break;
                    }
                }
            } else {
                // Check the first (numParamTypes - 1) argTypes against their corresponding paramTypes
                for (int i = 0; i < (numParamTypes - 1); ++i) {
                    TypeMirror paramType = paramTypes.get(i);
                    TypeMirror argType = argTypes.get(i);
                    if (types.isSameType(argType, paramType)) continue;
                    if (!isSameBoxedType(argType, paramType)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    // Check the remaining argTypes against the last (varArgs) paramType
                    TypeMirror varArgsParamType = ((ArrayType)paramTypes.get(numParamTypes - 1)).getComponentType();
                    for (int i = (numParamTypes - 1); i < numArgTypes; ++i) {
                        TypeMirror argType = argTypes.get(i);
                        if (types.isSameType(argType, varArgsParamType)) continue;
                        if (!isSameBoxedType(argType, varArgsParamType)) {
                            match = false;
                            break;
                        }
                    }
                }
            }
            return match;
        }

        private boolean assignableVarArgsMatch(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            boolean match = true;
            int numParamTypes = paramTypes.size();
            int numArgTypes = argTypes.size();
            if (numArgTypes < numParamTypes) {
                // Ignore the last (varArgs) parameter
                for (int i = 0; i < numArgTypes; ++i) {
                    if (!types.isAssignable(argTypes.get(i), paramTypes.get(i))) {
                        match = false;
                        break;
                    }
                }
            } else {
                // Check the first (numParamTypes - 1) argTypes against their corresponding paramTypes
                for (int i = 0; i < (numParamTypes - 1); ++i) {
                    if (!types.isAssignable(argTypes.get(i), paramTypes.get(i))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    // Check the remaining argTypes against the last (varArgs) paramType
                    TypeMirror varArgsParamType = ((ArrayType)paramTypes.get(numParamTypes - 1)).getComponentType();
                    for (int i = (numParamTypes - 1); i < numArgTypes; ++i) {
                        if (!types.isAssignable(argTypes.get(i), varArgsParamType)) {
                            match = false;
                            break;
                        }
                    }
                }
            }
            return match;
        }

        private int exactFixedArgsMatches(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            int matches = 0;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                if (types.isSameType(argTypes.get(i), paramTypes.get(i))) {
                    ++matches;
                }
            }
            return matches;
        }

        private int boxingFixedArgsMatches(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            int matches = 0;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argType = argTypes.get(i);
                TypeMirror paramType = paramTypes.get(i);
                if (types.isSameType(argType, paramType)) continue;
                if (isSameBoxedType(argType, paramType)) {
                    ++matches;
                }
            }
            return matches;
        }

        private int erasureFixedArgsMatches(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            int matches = 0;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argType = argTypes.get(i);
                TypeMirror paramType = paramTypes.get(i);
                if (types.isSameType(argType, paramType)) continue;
                if (isSameBoxedType(argType, paramType)) continue;
                if (isSameErasureType(argType, paramType)) {
                    ++matches;
                }
            }
            return matches;
        }

        private int directSupertypeFixedArgsMatches(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            int matches = 0;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argType = argTypes.get(i);
                TypeMirror paramType = paramTypes.get(i);
                if (types.isSameType(argType, paramType)) continue;
                if (isSameBoxedType(argType, paramType)) continue;
                if (isSameErasureType(argType, paramType)) continue;
                if (isDirectSupertype(paramType, argType)) {
                    ++matches;
                }
            }
            return matches;
        }

        private int indirectSupertypeMatches(List<? extends TypeMirror> paramTypes, List<TypeMirror> argTypes) {
            int matches = 0;
            int size = argTypes.size();
            for (int i = 0; i < size; ++i) {
                TypeMirror argErasure = types.erasure(argTypes.get(i));
                TypeMirror paramErasure = types.erasure(paramTypes.get(i));
                if (types.isSubtype(argErasure, paramErasure)) {
                    ++matches;
                }
            }
            return matches;
        }

        private List<ExecutableElement> filterDuplicateCandidates(List<ExecutableElement> candidates) {
            // Return the list of candidates with duplicates removed

            // Detect duplicates
            int candidatesSize = candidates.size();
            Map<String,List<ExecutableElement>> duplicates = new HashMap<String,List<ExecutableElement>>();
            Set<Integer> duplicateIndices = new TreeSet<Integer>();
            for (int i = 0; i < candidatesSize; ++i) {
                if (duplicateIndices.contains(i)) continue;
                ExecutableElement current = candidates.get(i);
                String currentStr = current.toString();
                for (int j = i + 1; j < candidatesSize; ++j) {
                    ExecutableElement possibleDup = candidates.get(j);
                    if (!possibleDup.toString().equals(currentStr)) continue;
                    duplicateIndices.add(j);
                    List<ExecutableElement> currentDups = duplicates.get(currentStr);
                    if (null == currentDups) {
                        currentDups = new ArrayList<ExecutableElement>();
                        currentDups.add(current);
                    }
                    currentDups.add(possibleDup);
                    duplicates.put(currentStr, currentDups);
                }
            }

            // Initialize nonDuplicates
            List<ExecutableElement> nonDuplicates = new ArrayList<ExecutableElement>();
            for (ExecutableElement candidate : candidates) {
                String candidateStr = candidate.toString();
                List<ExecutableElement> candidateDups = duplicates.get(candidateStr);
                if (null == candidateDups) {
                    nonDuplicates.add(candidate);
                }
            }

            // Handle duplicates by eliminating those that originate in an superinterface (in favor of those that originate in a superclass)
            for (Map.Entry<String,List<ExecutableElement>> entry : duplicates.entrySet()) {
                List<ExecutableElement> dups = entry.getValue();
                if (2 != dups.size()) {
                    throw new UnreachableException("Found " + dups.size() + " duplicates of " + entry.getKey());
                }
                List<ExecutableElement> classOriginators = new ArrayList<ExecutableElement>();
                for (ExecutableElement dup : dups) {
                    if (ElementKind.CLASS == dup.getEnclosingElement().getKind()) {
                        classOriginators.add(dup);
                    }
                }
                int classOriginatorsSize = classOriginators.size();
                if (0 == classOriginatorsSize) {
                    throw new UnreachableException("None of the duplicates of " + entry.getKey() + " originate in a class");
                }
                // If necessary, choose the most specific class
                ExecutableElement bestDuplicate = classOriginators.get(0);
                if (classOriginatorsSize > 1) {
                    for (int i = 1; i < classOriginatorsSize; ++i) {
                        TypeMirror bdType = bestDuplicate.getEnclosingElement().asType();
                        ExecutableElement element = classOriginators.get(i);
                        TypeMirror eType = element.getEnclosingElement().asType();
                        if (types.isSubtype(eType, bdType)) {
                            bestDuplicate = element;
                        } else if (!types.isSubtype(bdType, eType)) {
                            throw new UnreachableException("No relationship between duplicates originators for " + entry.getKey());
                        }
                    }
                }
                nonDuplicates.add(bestDuplicate);
            }

            return nonDuplicates;
        }

        private String callee(Tree node, List<ExecutableElement> candidateList, List<? extends ExpressionTree> args) {
            List<ExecutableElement> candidates = candidateList;
            int candidatesSize = candidates.size();

            // Sanity check
            if (0 == candidatesSize) {
                throw new UnreachableException("No candidates for " + node);
            }

            // Only one candidate
            if (1 == candidatesSize) {
                return candidates.get(0).toString();
            }

            // Remove duplicates from the candidate list
            candidates = filterDuplicateCandidates(candidates);

            int numArgs = args.size();
            // Look for candidates with a compatible number of parameters
            List<ExecutableElement> fixedArgsCandidates = new ArrayList<ExecutableElement>();
            List<ExecutableElement> varArgsCandidates = new ArrayList<ExecutableElement>();
            for (ExecutableElement candidate : candidates) {
                int numParams = candidate.getParameters().size();
                if (!candidate.isVarArgs()) {
                    if (numArgs == numParams) {
                        fixedArgsCandidates.add(candidate);
                    }
                } else {
                    if (numArgs == numParams) {
                        varArgsCandidates.add(candidate);
                    } else if (numArgs == (numParams - 1)) {
                        varArgsCandidates.add(candidate);
                    } else if (numArgs > numParams) {
                        varArgsCandidates.add(candidate);
                    }
                }
            }

            int numFixedArgsCandidates = fixedArgsCandidates.size();
            int numVarArgsCandidates = varArgsCandidates.size();
            boolean zeroFixedArgsCandidates = (0 == numFixedArgsCandidates);
            boolean zeroVarArgsCandidates = (0 == numVarArgsCandidates);
            // Sanity check
            if (zeroFixedArgsCandidates && zeroVarArgsCandidates) {
                throw new UnreachableException("No candidates with a compatible number of parameters for " + node);
            }

            // Only one candidate with the correct number of parameters
            if ((1 == numFixedArgsCandidates) && zeroVarArgsCandidates) {
                return fixedArgsCandidates.get(0).toString();
            } else if ((1 == numVarArgsCandidates) && zeroFixedArgsCandidates) {
                return varArgsCandidates.get(0).toString();
            }

            // Store arg types
            List<TypeMirror> argTypes = new ArrayList<TypeMirror>();
            for (ExpressionTree arg : args) {
                argTypes.add(((JCTree)arg).type);
            }

            //NOTE: Normal fixedArgs methods take precedence over varArgs methods

            // Look for an exact match
            for (ExecutableElement candidate : fixedArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (exactFixedArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                    return candidate.toString();
                }
            }
            // No exact match

            // Look for a match that is exact other than boxing
            for (ExecutableElement candidate : fixedArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (boxingFixedArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                    return candidate.toString();
                }
            }
            // No exact-other-than-boxing match

            // Look for candidates with assignable parameters
            candidates = new ArrayList<ExecutableElement>();
            for (ExecutableElement candidate : fixedArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (assignableFixedArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                    candidates.add(candidate);
                }
            }
            candidatesSize = candidates.size();

            // If necessary, look for candidates with assignable parameter erasures
            if (0 == candidatesSize) {
                for (ExecutableElement candidate : fixedArgsCandidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    if (assignableErasureFixedArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                        candidates.add(candidate);
                    }
                }
                candidatesSize = candidates.size();
            }

            // Only one candidate with assignable parameters
            if (1 == candidatesSize) {
                return candidates.get(0).toString();
            }

            // Return the best candidate
            if (1 < candidatesSize) {
                // Look for the candidate with the largest number of exact matches
                List<ExecutableElement> bestCandidates = new ArrayList<ExecutableElement>();
                int maxMatches = 0;
                for (ExecutableElement candidate : candidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = exactFixedArgsMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            bestCandidates.add(candidate);
                        }
                    }
                }
                int bestCandidatesSize = bestCandidates.size();

                if (1 == bestCandidatesSize) {
                    return bestCandidates.get(0).toString();
                }

                if (1 < bestCandidatesSize) {
                    candidates = bestCandidates;
                    candidatesSize = bestCandidatesSize;
                }

                // Look for the candidate with the largest number of exact-other-than-boxing matches
                bestCandidates = new ArrayList<ExecutableElement>();
                maxMatches = 0;
                for (ExecutableElement candidate : candidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = boxingFixedArgsMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();

                if (1 == bestCandidatesSize) {
                    return bestCandidates.get(0).toString();
                }

                if (1 < bestCandidatesSize) {
                    candidates = bestCandidates;
                    candidatesSize = bestCandidatesSize;
                }

                // Look for the candidate with the largest number of erasure matches
                bestCandidates = new ArrayList<ExecutableElement>();
                maxMatches = 0;
                for (ExecutableElement candidate : candidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = erasureFixedArgsMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();

                if (1 == bestCandidatesSize) {
                    return bestCandidates.get(0).toString();
                }

                if (1 < bestCandidatesSize) {
                    candidates = bestCandidates;
                    candidatesSize = bestCandidatesSize;
                }

                // Look for the candidate with the largest number of direct supertype matches
                bestCandidates = new ArrayList<ExecutableElement>();
                maxMatches = 0;
                for (ExecutableElement candidate : candidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = directSupertypeFixedArgsMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();

                if (1 == bestCandidatesSize) {
                    return bestCandidates.get(0).toString();
                }

                if (1 < bestCandidatesSize) {
                    candidates = bestCandidates;
                    candidatesSize = bestCandidatesSize;
                }

/*
                System.out.flush();
                System.out.println("Node is " + node);
                for (ExecutableElement candidate : candidates) {
                    System.out.println("\tCandidate " + candidate);
                }
                System.out.flush();
*/

                // Checking frequency of this case...
                if (2 < candidatesSize) {
                    throw new UnreachableException("Still have more than two bestCandidates");
                }

                // Look for the most specific candidate
                ExecutableElement e1 = candidates.get(0);
                ExecutableElement e2 = candidates.get(1);
                List<? extends TypeMirror> p1 = ((ExecutableType)e1.asType()).getParameterTypes();
                List<? extends TypeMirror> p2 = ((ExecutableType)e2.asType()).getParameterTypes();
                int size = p1.size();
                int specific = 0;
                for (int i = 0; i < size; ++i) {
                    TypeMirror t1 = p1.get(i);
                    TypeMirror t2 = p2.get(i);
                    if (types.isSameType(t1, t2)) continue;
                    if (isSameBoxedType(t1, t2)) continue;
                    if (isSameErasureType(t1, t2)) continue;
                    if (types.isSubtype(t1, t2)) {
                        ++specific;
                    } else if (types.isSubtype(t2, t1)) {
                        --specific;
                    }
                }
                if (0 < specific) {
                    System.out.flush();
                        System.out.println("Node is " + node);
                        for (ExecutableElement candidate : candidates) {
                            System.out.println("\tCandidate " + candidate);
                        }
                    System.out.println("Picking " + e1);
                    System.out.flush();
                    return e1.toString();
                } else if (0 > specific) {
                    System.out.flush();
                        System.out.println("Node is " + node);
                        for (ExecutableElement candidate : candidates) {
                            System.out.println("\tCandidate " + candidate);
                        }
                    System.out.println("Picking " + e2);
                    System.out.flush();
                    return e2.toString();
                }
                // Not a specificity issue

                // Check for primitives
                for (int i = 0; i < size; ++i) {
                    TypeMirror t1 = p1.get(i);
                    TypeMirror t2 = p2.get(i);
                    if (types.isSameType(t1, t2)) continue;
                    if (isSameBoxedType(t1, t2)) continue;
                    if (isSameErasureType(t1, t2)) continue;
                    TypeKind k1 = t1.getKind();
                    TypeKind k2 = t2.getKind();
                    boolean isPrimitive1 = k1.isPrimitive();
                    boolean isPrimitive2 = k2.isPrimitive();
                    if (isPrimitive1 && (!isPrimitive2)) {
                        System.out.println("Picking " + e1 + " over " + e2);
                        System.out.flush();
                        return e1.toString();
                    } else if ((!isPrimitive1) && isPrimitive2) {
                        System.out.println("Picking " + e2 + " over " + e1);
                        System.out.flush();
                        return e2.toString();
                    }
                }
                // Not a primitives issue

                System.out.flush();
                System.out.println("Shitty Node is " + node);
                for (ExecutableElement candidate : candidates) {
                    System.out.println("\tShitty Candidate " + candidate);
                }
                System.out.flush();
                throw new UnreachableException("Shitty heuristic");

                //throw new UnreachableException("Still getting here");
            }
            // No fixedArgs candidates, move on to varArgs candidates

            // Sanity check
            if (0 == numVarArgsCandidates) {
                throw new UnreachableException("No fixedArgs candidates, but varArgsCandidates is empty");
            }

            // Only one candidate
            if (1 == numVarArgsCandidates) {
                return varArgsCandidates.get(0).toString();
            }

            // Look for a varargs exact match
            for (ExecutableElement candidate : varArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (candidate.isVarArgs()) {
                    if (exactVarArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                        return candidate.toString();
                    }
                }
            }
            // No varargs exact match

            // Look for a match that is exact other than boxing
            for (ExecutableElement candidate : varArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (boxingVarArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                    return candidate.toString();
                }
            }
            // No exact-other-than-boxing match

            // Look for candidates with assignable parameters
            candidates = new ArrayList<ExecutableElement>();
            for (ExecutableElement candidate : varArgsCandidates) {
                ExecutableType candidateType = (ExecutableType)(candidate.asType());
                if (assignableVarArgsMatch(candidateType.getParameterTypes(), argTypes)) {
                    candidates.add(candidate);
                }
            }
            candidatesSize = candidates.size();

            // If necessary, look for candidates with assignable parameter erasures
            if (0 == candidatesSize) {
                throw new UnreachableException("Need to check for assignable erasure parameters (varArgs)");
            }

            // Only one candidate with assignable parameters
            if (1 == candidatesSize) {
                return candidates.get(0).toString();
            }

            // Return the best candidate
            if (1 < candidatesSize) {
                throw new UnreachableException("Need to return the best (varArgs) candidate");
            }

            // No matches (impossible)
            throw new UnreachableException("callee(...) ended without hitting a return statement");
/*
            // Look for the candidate with the largest number of erasure matches
            if (0 == bestCandidatesSize) {
                for (ExecutableElement candidate : assignableCandidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = erasureMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            //throw new UnreachableException("Two erasure matches in assignableCandidates");
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();
            }

            // Look for the candidate with the largest number of direct supertype matches
            if (0 == bestCandidatesSize) {
                for (ExecutableElement candidate : assignableCandidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = directSupertypeMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            //throw new UnreachableException("Two direct supertype matches in assignableCandidates");
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();
            }

            // Look for the candidate with the largest number of indirect supertype matches
            if (0 == bestCandidatesSize) {
                for (ExecutableElement candidate : assignableCandidates) {
                    ExecutableType candidateType = (ExecutableType)(candidate.asType());
                    int matches = indirectSupertypeMatches(candidateType.getParameterTypes(), argTypes);
                    if (matches > 0) {
                        if (matches > maxMatches) {
                            maxMatches = matches;
                            bestCandidates.clear();
                            bestCandidates.add(candidate);
                        } else if (matches == maxMatches) {
                            bestCandidates.add(candidate);
                        }
                    }
                }
                bestCandidatesSize = bestCandidates.size();
            }

            if (1 < bestCandidatesSize) {
                debug(">1 BC", node);
                for (ExecutableElement candidate : bestCandidates) {
                    System.out.println(">1 BC cand: " + candidate);
                }
                System.out.flush();
                throw new UnreachableException("More than one best candidate found");
            }

            //System.out.println("\tBest Assignable Callee: " + bestCandidate);
            return bestCandidates.get(0).toString();
*/
        }

        private void debug(String prefix, Tree node) {
            System.out.flush();
            System.out.println(prefix + " node: " + node);
            System.out.println(prefix + " file: " + cut.getSourceFile().getName());
            System.out.flush();
        }

        @Override
        public Void visitNewClass(NewClassTree node, Void ignored) {
            super.visitNewClass(node, ignored);

            TreePath path = TreePath.getPath(cut, node);

            ExpressionTree identifier = node.getIdentifier();
            Tree.Kind kind = identifier.getKind();
            if (kind == Tree.Kind.IDENTIFIER || kind == Tree.Kind.PARAMETERIZED_TYPE || kind == Tree.Kind.MEMBER_SELECT) {
                TypeElement classElement = (TypeElement)types.asElement(((JCTree)identifier).type);
                if (ElementKind.INTERFACE == classElement.getKind()) {
                    //System.out.println("INSTANTIATE ANON CLASS IMPL of INTERFACE");
                    return null;
                }
                List<? extends Element> classMembers = elements.getAllMembers(classElement);
                List<ExecutableElement> constructors = ElementFilter.constructorsIn(classMembers);
                String caller = caller(path);
                if (!"<CLASS_BODY>".equals(caller)) {
                    //System.out.println("Caller: " + caller);
                    String callee = callee(node, constructors, node.getArguments());
                    //System.out.println("\tCallee: " + classElement.getQualifiedName() + "." + callee + "\n");
                } else {
                    //System.err.println("Was in class body");
                }
            //} else if (kind == Tree.Kind.MEMBER_SELECT) {
                //System.err.println("MEMSEL: " + cut.getSourceFile().getName());
                //System.err.println("MEMSEL: " + node);
                //System.err.println("MEMSEL: " + identifier);
            } else {
                throw new UnreachableException("visitNewClass: kind == " + kind);
            }
            return null;
        }
    }
}
