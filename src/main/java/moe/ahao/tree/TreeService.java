package moe.ahao.tree;

import java.util.List;

public class TreeService<T extends TreeNode> {
    private final TreeRepository<T> treeRepository;

    public TreeService(TreeRepository<T> treeRepository) {
        this.treeRepository = treeRepository;
    }

    public T getOne(long id) {
        return treeRepository.selectOneById(id);
    }

    public List<T> getChildren(long id) {
        T node = this.getOne(id);

        long left = node.getLeft();
        long right = node.getRight();
        int level = node.getLevel();

        List<T> treeNodes = treeRepository.selectChildren(left, right, level + 1);
        return treeNodes;
    }

    public List<T> getDescendant(long id) {
        T node = this.getOne(id);

        long left = node.getLeft();
        long right = node.getRight();

        List<T> treeNodes = treeRepository.selectDescendantList(left, right);
        return treeNodes;
    }

    public long getDescendantCount(long id) {
        T node = this.getOne(id);

        long left = node.getLeft();
        long right = node.getRight();

        long count = (right - left - 1) / 2;
        return count;
    }

    public List<T> getAncestorLink(long id) {
        T node = this.getOne(id);

        long left = node.getLeft();
        long right = node.getRight();

        List<T> treeNodes = treeRepository.selectAncestor(left, right);
        return treeNodes;
    }

    boolean isLeaf(long id) {
        T node = this.getOne(id);

        long left = node.getLeft();
        long right = node.getRight();

        return right - left == 1;
    }

    long save(long parentId, T node) {
        T parentNode = this.getOne(parentId);

        node.setParentId(parentId);
        node.setLeft(parentNode.getRight());
        node.setRight(parentNode.getRight() + 1);
        node.setLevel(parentNode.getLevel() + 1);

        long left = node.getLeft();
        treeRepository.updateLeftWhenAdd(left);
        treeRepository.updateRightWhenAdd(left);

        treeRepository.insertOne(node);

        return node.getId();
    }

    void delete(long id) {
        T node = this.getOne(id);
        if(node == null) {
            return;
        }

        long left = node.getLeft();
        long right = node.getRight();

        treeRepository.delete(left, right);

        long decrease = right - left + 1;
        treeRepository.updateLeftWhenDelete(decrease, right);
        treeRepository.updateRightWhenDelete(decrease, left);
    }
}
