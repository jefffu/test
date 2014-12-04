package org.jfu.test.jgit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuildIterator;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.resolver.FileResolver;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeIterator;
import org.junit.Test;

public class TestRepository {

    @Test
    public void test() throws IOException, ServiceNotEnabledException, InvalidRemoteException, TransportException, GitAPIException {
        FileResolver<String> resolver = new FileResolver<String>(
                new File("/src/test/git/local"), true);
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

            DirCacheBuildIterator i = tw.getTree(0, DirCacheBuildIterator.class);
            WorkingTreeIterator f = tw.getTree(1, WorkingTreeIterator.class);
            System.out.println(i + "/" + f);
            if (i != null) {
                DirCacheEntry dce = i.getDirCacheEntry();
                System.out.println("dir cache entry stage: " + (dce != null ? dce.getStage() : " - ") + ", creation time: " + new Date(dce.getCreationTime()));
            }
            System.out.println("========");
        }
        dc.unlock();
    }

    public void test1()  throws IOException, ServiceNotEnabledException, InvalidRemoteException, TransportException, GitAPIException {
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
