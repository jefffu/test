package org.jfu.test.jgit;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseCommand.InteractiveHandler;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuildIterator;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.dircache.DirCacheTree;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.MutableObjectId;
import org.eclipse.jgit.lib.ObjectDatabase;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.resolver.FileResolver;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeIterator;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.util.QuotedString;
import org.junit.Test;

public class TestRepository {

    private void printDir(DirCache dc, String dir, DirCacheTree dct) {
        if (dct.getPathString().equals(dir)) {
            for (int i=0; i<dct.getChildCount(); i++) {
                System.out.println(dct.getChild(i).getNameString());
            }

            // List Children of Blob
            DirCacheEntry[] entries = dc.getEntriesWithin(dct.getPathString());

            if (entries != null) {
                for (DirCacheEntry entry : entries) {

                    String pathString = entry.getPathString();
                    String fileName = pathString.substring(dct.getPathString().length());
                    if (fileName.indexOf("/") <= 0) {
                        System.out.println(fileName);
                    }
                }
            }
        } else {
            for (int i=0; i<dct.getChildCount(); i++) {
                printDir(dc, dir, dct.getChild(i));
            }
        }
    }

    private void print(DirCache dc, int depth, DirCacheTree dct) {
        for (int i=0; i<depth*2; i++) {
            System.out.print(" ");
        }
        System.out.println(dct.getNameString());
        // List Children of Tree
        for (int i=0; i<dct.getChildCount(); i++) {
            print(dc, depth+1, dct.getChild(i));
        }

        // List Children of Blob
        DirCacheEntry[] entries = dc.getEntriesWithin(dct.getPathString());

        if (entries != null) {
            for (DirCacheEntry entry : entries) {

                String pathString = entry.getPathString();
                String fileName = pathString.substring(dct.getPathString().length());
                if (fileName.indexOf("/") <= 0) {
                    for (int i=0; i<(depth+1)*2; i++) {
                        System.out.print(" ");
                    }
                    System.out.println(fileName);
                }
            }
        }
    }

    public void testDirCache() throws ServiceNotEnabledException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");
        DirCache dc = DirCache.newInCore();
        DirCacheBuilder builder = dc.builder();

        TreeWalk treeWalk = new TreeWalk(repo);
        DirCacheBuildIterator dcbIterator = new DirCacheBuildIterator(builder);
        treeWalk.addTree(dcbIterator);

        String branch = "master";
        ObjectId commitId = repo.resolve(branch);

        RevWalk revWalk = new RevWalk(repo);
        RevCommit curCommit = revWalk.parseCommit(commitId);

        RevTree revTree = curCommit.getTree();
        treeWalk.addTree(revTree);
        treeWalk.setRecursive(true);

        while (treeWalk.next()) {
            CanonicalTreeParser ctp = treeWalk.getTree(1,
                    CanonicalTreeParser.class);
            String path = treeWalk.getPathString();
            System.out.println(path);
            DirCacheEntry entry = new DirCacheEntry(path);
            entry.setFileMode(ctp.getEntryFileMode());
                entry.setObjectId(ctp.getEntryObjectId());
            builder.add(entry);
        }

        builder.finish();

        DirCacheTree dct = dc.getCacheTree(true);

//        print(dc, 0, dct);
//        System.out.println("========================");
        printDir(dc, "a/", dct);
        repo.close();

    }

    public void testTreeWalk() throws ServiceNotEnabledException, IOException {
        String branch = "master";
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        ObjectReader reader = repo.newObjectReader();

        ObjectId commitId = repo.resolve(branch);
        System.out.println(commitId);

        RevWalk revWalk = new RevWalk(repo);

        RevCommit revCommit = revWalk.parseCommit(commitId);

        RevTree revTree = revCommit.getTree();

        CanonicalTreeParser parser = new CanonicalTreeParser();
        parser.reset(reader, revTree.getId());
        FileMode fileMode = parser.getEntryFileMode();
        System.out.println(fileMode);
        System.out.println(parser.getEntryPathString());

        repo.close();
    }

    private RevTree getRevTree(RevWalk revWalk, TreeWalk treeWalk, AnyObjectId commitId, String dir) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
        if (dir.length() == 0) {
            RevCommit revCommit = revWalk.parseCommit(commitId);

            RevTree revTree = revCommit.getTree();
            return revTree;

        } else {
            Path path = Paths.get(dir);
            Path parent = path.getParent();
            String parentDir = "";
            if (parent != null) {
                parentDir = parent.toString();
            }
            RevTree revTree = getRevTree(revWalk, treeWalk, commitId, parentDir);
            treeWalk.reset();
            treeWalk.setRecursive(false);

            treeWalk.addTree(revTree);
            while (treeWalk.next()) {
                FileMode mode = treeWalk.getFileMode(0);
                if (mode == FileMode.TREE) {
                    if(treeWalk.getPathString().equals(path.getFileName().toString())) {
                        ObjectId treeId = treeWalk.getObjectId(0);
                        return revWalk.parseTree(treeId);
                    }
                }
            }
        }
        return null;
    }

    public void testLs2() throws ServiceNotEnabledException, IOException {

        String dir = "dir";

        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        ObjectId commitId = repo.resolve("master");

        RevWalk revWalk = new RevWalk(repo);
        RevCommit revCommit = revWalk.parseCommit(commitId);

        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.addTree(revCommit.getTree());
        treeWalk.setRecursive(false);

        boolean found = true;
        if (dir.length() != 0) {
            treeWalk.setFilter(PathFilterGroup.createFromStrings(dir));

            Path path = Paths.get(dir);
            Iterator<Path> iter = path.iterator();
            while (iter.hasNext()) {
                String pathName = iter.next().getFileName().toString();
                while (treeWalk.next()) {
                    FileMode mode = treeWalk.getFileMode(0);
                    if (mode.equals(FileMode.TREE) && treeWalk.getNameString().equals(pathName)) {
                        treeWalk.enterSubtree();
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
            }
        }

        if (found) {
            PrintStream outw = System.out;
            while (treeWalk.next()) {
                final FileMode mode = treeWalk.getFileMode(0);
                if (mode == FileMode.TREE)
                    outw.print('0');
                outw.print(mode);
                outw.print(' ');
                outw.print(Constants.typeString(mode.getObjectType()));

                outw.print(' ');
                outw.print(treeWalk.getObjectId(0).name());

                outw.print('\t');
                outw.print(QuotedString.GIT_PATH.quote(treeWalk.getPathString()));
                outw.print("("+treeWalk.getPathString()+")");
                outw.print("("+treeWalk.getNameString()+")");
                outw.println();
            }
        }

        repo.close();

    }

    public void testLs() throws ServiceNotEnabledException, IOException {
        String dir = "dir";

        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        ObjectId commitId = repo.resolve("master");

        RevWalk revWalk = new RevWalk(repo);

        TreeWalk treeWalk = new TreeWalk(repo);

        RevTree revTree = getRevTree(revWalk, treeWalk, commitId, dir);

        treeWalk.reset();
        treeWalk.setRecursive(false);
        treeWalk.addTree(revTree);

        PrintStream outw = System.out;

        while (treeWalk.next()) {
            final FileMode mode = treeWalk.getFileMode(0);
            if (mode == FileMode.TREE)
                outw.print('0');
            outw.print(mode);
            outw.print(' ');
            outw.print(Constants.typeString(mode.getObjectType()));

            outw.print(' ');
            outw.print(treeWalk.getObjectId(0).name());

            outw.print('\t');
            outw.print(QuotedString.GIT_PATH.quote(treeWalk.getPathString()));
            outw.print("("+treeWalk.getPathString()+")");
            outw.print("("+treeWalk.getNameString()+")");
            outw.println();
        }


        repo.close();

    }

    // TODO
    @Test
    public void testContainsObjectIdInCommit() throws ServiceNotEnabledException, IOException {
        String dir = "";

        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        ObjectId commitId = repo.resolve("master");

        RevWalk revWalk = new RevWalk(repo);
        RevCommit revCommit = revWalk.parseCommit(commitId);

        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.addTree(revCommit.getTree());
        treeWalk.setRecursive(false);

        boolean found = true;
        if (dir.length() != 0) {
            treeWalk.setFilter(PathFilterGroup.createFromStrings(dir));

            Path path = Paths.get(dir);
            Iterator<Path> iter = path.iterator();
            while (iter.hasNext()) {
                String pathName = iter.next().getFileName().toString();
                while (treeWalk.next()) {
                    FileMode mode = treeWalk.getFileMode(0);
                    if (mode.equals(FileMode.TREE) && treeWalk.getNameString().equals(pathName)) {
                        treeWalk.enterSubtree();
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
            }
        }

        Set<AnyObjectId> set = new HashSet<>();
        if (found) {
            PrintStream outw = System.out;
            while (treeWalk.next()) {
                final FileMode mode = treeWalk.getFileMode(0);
                if (mode == FileMode.TREE)
                    outw.print('0');
                outw.print(mode);
                outw.print(' ');
                outw.print(Constants.typeString(mode.getObjectType()));

                outw.print(' ');
                outw.print(treeWalk.getObjectId(0).name());
                set.add(treeWalk.getObjectId(0));

                outw.print('\t');
                outw.print(QuotedString.GIT_PATH.quote(treeWalk.getPathString()));
                outw.print("("+treeWalk.getPathString()+")");
                outw.print("("+treeWalk.getNameString()+")");
                outw.println();
            }
        }

        System.out.println(set);

        System.out.println("master:" + commitId);

//        print(revWalk, revCommit);

        contains(revWalk, revCommit, treeWalk, set);
        repo.close();

    }

    private void contains(RevWalk revWalk, RevCommit revCommit, TreeWalk treeWalk, Set<AnyObjectId> anyObjectIds) throws MissingObjectException, IncorrectObjectTypeException, IOException {
        for (RevCommit p : revCommit.getParents()) {
            revWalk.parseCommit(p.getId());

            treeWalk.reset();
            treeWalk.addTree(p.getTree());
            treeWalk.setRecursive(false);
            Set<AnyObjectId> result = contains(treeWalk, anyObjectIds);

            System.out.println(p.getId() + " contains objects: " + result);

            if (result.size() > 0) {
                contains(revWalk, p, treeWalk, result);
            }
        }
    }

    private void print(RevWalk revWalk, RevCommit revCommit) throws MissingObjectException, IncorrectObjectTypeException, IOException {
        for (RevCommit p : revCommit.getParents()) {
            System.out.println(p.getId());
            revWalk.parseCommit(p.getId());
            print(revWalk, p);
        }
    }

    private Set<AnyObjectId> contains(TreeWalk treeWalk, Set<AnyObjectId> anyObjectIds) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {

        Set<AnyObjectId> result = new HashSet<>();

        while (treeWalk.next()) {
            ObjectId objectId = treeWalk.getObjectId(0);
            if (anyObjectIds.contains(objectId)) {
                result.add(objectId);
            }
            FileMode mode = treeWalk.getFileMode(0);

            if (mode.equals(FileMode.TREE) ) {
                treeWalk.enterSubtree();
            }
        }

        return result;
    }





    public void testReadRefForContributor() throws ServiceNotEnabledException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        RefDatabase refDb = repo.getRefDatabase();

        Ref ref = refDb.getRef("refs/groups/contributor");
        if (ref != null) {
            ObjectId objectId = ref.getObjectId();

            ObjectDatabase objDb = repo.getObjectDatabase();
            System.out.println(new String(objDb.open(objectId).getBytes()));
        }

        repo.close();
    }

    public void testCreateRefForContributor() throws ServiceNotEnabledException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        ObjectDatabase objDb = repo.getObjectDatabase();
        ObjectInserter inserter = objDb.newInserter();

        ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, "jefffu".getBytes());

        RefDatabase refDb = repo.getRefDatabase();

        RefUpdate refUpdate = refDb.newUpdate("refs/groups/contributor", true);
        refUpdate.setNewObjectId(objectId);
        refUpdate.update();

        repo.close();
    }

    public void testDiff() throws ServiceNotEnabledException, IOException,
            GitAPIException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        Git git = new Git(repo);
        DiffCommand diffCommand = git.diff();
        String branch = "master";
        Ref ref = repo.getRef("refs/heads/" + branch);
        RevWalk revWalk = new RevWalk(repo);
        RevCommit curCommit = revWalk.parseCommit(ref.getObjectId());

        ObjectReader reader = repo.newObjectReader();

        RevTree revTree = curCommit.getTree();
        CanonicalTreeParser newTreeParser = new CanonicalTreeParser();
        newTreeParser.reset(reader, revTree.getId());

        MutableObjectId commitId = new MutableObjectId();
        commitId.fromString("b1e3d3c5d1afc6e83869778eff9199f1f01ec2fd");

        RevCommit preCommit = revWalk.parseCommit(commitId);
        RevTree preTree = preCommit.getTree();
        CanonicalTreeParser preTreeParser = new CanonicalTreeParser();
        preTreeParser.reset(reader, preTree.getId());

        diffCommand.setOldTree(preTreeParser);
        diffCommand.setNewTree(newTreeParser);

        List<DiffEntry> diffEnties = diffCommand.call();

        for (DiffEntry de : diffEnties) {
            System.out.println(de.getOldPath());
            System.out.println(de.getNewPath());

            System.out.println(de.getChangeType());
        }

    }

    public void testCommitByDirCache() throws ServiceNotEnabledException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");

        DirCache dc = DirCache.newInCore();
        DirCacheBuilder builder = dc.builder();

        TreeWalk treeWalk = new TreeWalk(repo);
        DirCacheBuildIterator dcbIterator = new DirCacheBuildIterator(builder);
        treeWalk.addTree(dcbIterator);

        String branch = "master";
        Ref ref = repo.getRef("refs/heads/" + branch);
        RevWalk revWalk = new RevWalk(repo);
        RevCommit curCommit = revWalk.parseCommit(ref.getObjectId());

        RevTree revTree = curCommit.getTree();
        treeWalk.addTree(revTree);
        treeWalk.setRecursive(true);

        String dir = "test";
        String fileName = "b.txt";
        String content = "TEST B";
        String username = "jefffu";
        String userEmail = "jefffu@qq.com";

        ObjectInserter inserter = repo.newObjectInserter();

        ObjectId newObjId = inserter.insert(Constants.OBJ_BLOB,
                content.getBytes("utf8"));

        Path filePath = Paths.get(dir, fileName);
        System.out.println(filePath.toString());

        boolean replace = false;
        while (treeWalk.next()) {
            CanonicalTreeParser ctp = treeWalk.getTree(1,
                    CanonicalTreeParser.class);
            String path = treeWalk.getPathString();
            DirCacheEntry entry = new DirCacheEntry(path);
            entry.setFileMode(ctp.getEntryFileMode());
            if (path.equals(filePath.toString())) {
                entry.setObjectId(newObjId);
                replace = true;
            } else {
                entry.setObjectId(ctp.getEntryObjectId());
            }
            builder.add(entry);
        }

        if (!replace) {
            DirCacheEntry entry = new DirCacheEntry(filePath.toString());
            entry.setFileMode(FileMode.REGULAR_FILE);
            entry.setObjectId(newObjId);
            builder.add(entry);
        }

        builder.finish();

        System.out.println(dc.getEntryCount());
        System.out.println(dc.getCacheTree(true).getChildCount());

        ObjectId treeId = dc.writeTree(inserter);

        CommitBuilder commitBuilder = new CommitBuilder();
        PersonIdent author = new PersonIdent(username, userEmail);
        commitBuilder.setAuthor(author);
        commitBuilder.setCommitter(author);
        commitBuilder.setTreeId(treeId);
        commitBuilder.setMessage("Test Message");
        commitBuilder.setParentId(curCommit.getId());
        ObjectId commitId = inserter.insert(commitBuilder);

        RefDatabase refDb = repo.getRefDatabase();
        RefUpdate refUpdate = refDb.newUpdate("refs/heads/" + branch, true);
        refUpdate.setNewObjectId(commitId);
        refUpdate.update();

        repo.close();

    }

    public void testCommit() throws RepositoryNotFoundException,
            ServiceNotEnabledException, NoHeadException, NoMessageException,
            UnmergedPathsException, ConcurrentRefUpdateException,
            WrongRepositoryStateException, GitAPIException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp"), true);
        Repository repo = resolver.open(null, "test");
        Git git = new Git(repo);

        PersonIdent author = new PersonIdent("jefffu", "jefffu@qq.com");

        CommitCommand commit = git.commit();

        commit.setAuthor(author);
        commit.setCommitter(author);
        commit.setMessage("Test Message");

        commit.call();

        repo.close();
    }

    public void test3() throws ServiceNotEnabledException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp/git"), true);
        Repository repo = resolver.open(null, "test.git");

        String branch = "master";

        Ref ref = repo.getRef("refs/heads/" + branch);

        RevWalk revWalk = new RevWalk(repo);
        RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());

        RevTree revTree = revCommit.getTree();

        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.setRecursive(true);
        treeWalk.addTree(revTree);

        TreeFormatter treeFormatter = new TreeFormatter();
        while (treeWalk.next()) {
            ObjectId objectId = treeWalk.getObjectId(0);
            String pathString = treeWalk.getPathString();
            Path path = Paths.get(pathString);
        }

        repo.close();
    }

    public TreeFormatter createTreeFormatter(TreeFormatter treeFormatter,
            Path filePath, ObjectId objectId) {
        return null;
    }

    public void commitNewFile() throws ServiceNotEnabledException,
            UnsupportedEncodingException, IOException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/tmp/git"), true);
        Repository repo = resolver.open(null, "test.git");

        Map<String, Ref> refsMap = repo.getAllRefs();
        for (Map.Entry<String, Ref> entry : refsMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());

        }

        ObjectDatabase db = repo.getObjectDatabase();
        ObjectInserter inserter = db.newInserter();

        String dir = "/test";
        String fileName = "b.txt";
        String content = "TEST B";
        String branch = "master";
        String username = "jefffu";
        String userEmail = "jefffu@qq.com";

        ObjectId treeId = createTree(inserter, dir, fileName,
                content.getBytes("utf8"));

        CommitBuilder builder = new CommitBuilder();
        PersonIdent author = new PersonIdent(username, userEmail);
        builder.setAuthor(author);
        builder.setCommitter(author);
        builder.setTreeId(treeId);
        builder.setMessage("Test Message");
        ObjectId commitId = inserter.insert(builder);

        RefDatabase refDb = repo.getRefDatabase();
        RefUpdate refUpdate = refDb.newUpdate("refs/heads/" + branch, true);
        refUpdate.setNewObjectId(commitId);
        refUpdate.update();

        repo.close();
    }

    private ObjectId createTree(ObjectInserter inserter, String dir,
            String fileName, byte[] fileContent) throws IOException {
        Path path = Paths.get(dir, fileName);
        ObjectId objId = inserter.insert(Constants.OBJ_BLOB, fileContent);

        ObjectId treeId = createTree(inserter, path, FileMode.REGULAR_FILE,
                objId);

        return treeId;
    }

    private ObjectId createTree(ObjectInserter inserter, Path path,
            FileMode fileMode, AnyObjectId anyObjectId) throws IOException {

        TreeFormatter treeFormatter = new TreeFormatter();
        treeFormatter.append(path.getFileName().toString(), fileMode,
                anyObjectId);

        ObjectId treeId = inserter.insert(treeFormatter);

        Path parent = path.getParent();
        if (path.getRoot().equals(parent)) {
            return treeId;
        } else {
            return createTree(inserter, parent, FileMode.TREE, treeId);
        }
    }

    public static void main(String[] args) {
//        System.out.println("*" + System.getProperty("line.separator") + "*");
//        Path path = Paths.get("dir/a/");
//        Iterator<Path> iter = path.iterator();
//        while (iter.hasNext()) {
//            Path p = iter.next();
//            System.out.print(p.getFileName().toString());
//            System.out.print(" == ");
//
//            System.out.println(p.toString());
//        }
        // TODO

        Set<AnyObjectId> set = new HashSet<>();

        MutableObjectId obj = new MutableObjectId();
        obj.fromString("930336e704755cb757be196612bc0e01ce0bb330");
        set.add(obj);

        obj = new MutableObjectId();
        obj.fromString("b1e3d3c5d1afc6e83869778eff9199f1f01ec2fd");
        set.add(obj);

        obj = new MutableObjectId();
        obj.fromString("b1e3d3c5d1afc6e83869778eff9199f1f01ec2fd");
        System.out.println(set.contains(obj));

        obj = new MutableObjectId();
        obj.fromString("b1e3d3c5d1afc6e83869778eff9199f1f01ec2ee");
        System.out.println(set.contains(obj));
    }

    public void test() throws IOException, ServiceNotEnabledException,
            InvalidRemoteException, TransportException, GitAPIException {
        FileResolver<String> resolver = new FileResolver<String>(new File(
                "/src/test/git/local"), true);
        Repository repo = resolver.open(null, "test");

        DirCache dc = repo.lockDirCache();
        DirCacheBuilder builder = dc.builder();

        WorkingTreeIterator workingTreeIterator = new FileTreeIterator(repo);

        final TreeWalk tw = new TreeWalk(repo);
        tw.addTree(new DirCacheBuildIterator(builder));
        tw.addTree(workingTreeIterator);
        tw.setRecursive(true);
        while (tw.next()) {
            ObjectId objectId = tw.getObjectId(0);
            System.out.println(objectId);
            Path path = Paths.get("/", tw.getPathString());
            System.out.println(path.getParent());
            System.out.println(path.getFileName());

            DirCacheBuildIterator i = tw
                    .getTree(0, DirCacheBuildIterator.class);
            WorkingTreeIterator f = tw.getTree(1, WorkingTreeIterator.class);
            System.out.println(i + "/" + f);
            if (i != null) {
                DirCacheEntry dce = i.getDirCacheEntry();
                System.out
                        .println("dir cache entry stage: "
                                + (dce != null ? dce.getStage() : " - ")
                                + ", creation time: "
                                + new Date(dce.getCreationTime()));
            }
            System.out.println("========");
        }
        dc.unlock();
    }

    public void test1() throws IOException, ServiceNotEnabledException,
            InvalidRemoteException, TransportException, GitAPIException {
        FileResolver<RepositoryResolver> resolver = new FileResolver<RepositoryResolver>(
                new File("/tmp/git/repo"), true);

        Repository repo = resolver.open(null, "test.git");

        for (Map.Entry<String, Ref> entry : repo.getAllRefs().entrySet()) {
            if (entry.getValue().isSymbolic()) {
                continue;
            }
            Ref ref = entry.getValue();
            System.out.println(entry.getKey());
            System.out.println(ref.getClass());
            ref = repo.peel(ref);
            System.out.println(ref.getClass());

            RevWalk revWalk = new RevWalk(repo);
            RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());
            System.out.println(revCommit);

            RevTree revTree = revCommit.getTree();
            System.out.println(revTree);

            TreeWalk treeWalk = new TreeWalk(repo);
            treeWalk.setRecursive(true);
            treeWalk.addTree(revTree);

            while (treeWalk.next()) {
                ObjectId objectId = treeWalk.getObjectId(0);
                System.out.println("---- " + objectId);
                Path path = Paths.get("/", treeWalk.getPathString());
                System.out.println(path.getParent());
                System.out.println(path.getFileName());
                System.out.println(new String(treeWalk.getObjectReader()
                        .open(objectId).getBytes()));
            }
            System.out.println("========");

        }

        System.out.println("==== clone...");
        CloneCommand clone = Git.cloneRepository();
        clone.setBare(true);
        clone.setCloneAllBranches(true);
        clone.setDirectory(new File("/tmp/git/repo/jefffu/test.git"));
        clone.setURI("file:/tmp/git/repo/test.git");
        clone.call();

    }
}
