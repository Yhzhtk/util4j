package yh.util.repeat;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试递归和迭代的效率
 * @author yh
 *
 */
public class TreeFilter {

	private Tree root = new Tree(' ');
	
	 public boolean contains(String value){
		 int len = value.length();
		 Tree t = root;
		 for(int i = 0; i < len; i++){
			 t = getChild(t, value.charAt(i));
			 if(t == null){
				 return false;
			 }
		 }
		 if(t == null){
			 return false;
		 }else{
			 return true;
		 }
	 }
	
	public Tree add(String value){
		int len = value.length();
		Tree p = root;
		Tree t = null;
		for(int i = 0; i < len; i++){
			t = getChild(p, value.charAt(i));
			if(t == null){
				t = new Tree(value.charAt(i));
				p.children.add(t);
			}
			p = t;
		}
		return t;
	}
	
	public Tree getChild(Tree parent, char value){
		List<Tree> children = parent.children;
		if(children == null || children.size() == 0){
			return null;
		}
		
		for(Tree c : children){
			if(c.value == value){
				return c;
			}
		}
		return null;
	}
	 
	/**
	 *  用list表示子节点
	 * @author yh
	 *
	 */
	public class Tree {
		
		private char value;
		private List<Tree> children;

		public Tree(char value){
			this.value = value;
			children = new ArrayList<Tree>();
		}
		
		public char getValue() {
			return value;
		}

		public void setValue(char value) {
			this.value = value;
		}

		public List<Tree> getChildren() {
			return children;
		}

		public void setChildren(List<Tree> children) {
			this.children = children;
		}
		
		public void addChild(Tree c){
			children.add(c);
		}
	}
}
