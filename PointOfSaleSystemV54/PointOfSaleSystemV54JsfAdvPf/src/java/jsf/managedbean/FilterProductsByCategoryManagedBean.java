package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;



@Named(value = "filterProductsByCategoryManagedBean")
@ViewScoped

public class FilterProductsByCategoryManagedBean implements Serializable
{
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
        
    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    
    private List<ProductEntity> productEntities;
    private ProductEntity selectedProductEntityToView;
    
    
    
    public FilterProductsByCategoryManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<CategoryEntity> categoryEntities = categoryEntityControllerLocal.retrieveAllRootCategories();
        treeNode = new DefaultTreeNode("Root", null);
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            createTreeNode(categoryEntity, treeNode);
        }
        
        Long selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCategory");
        
        if(selectedCategoryId != null)
        {
            for(TreeNode tn:treeNode.getChildren())
            {
                CategoryEntity ce = (CategoryEntity)tn.getData();

                if(ce.getCategoryId().equals(selectedCategoryId))
                {
                    selectedTreeNode = tn;
                    break;
                }
                else
                {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }            
            }
        }
        
        filterProduct();
    }
    
    
    
    public void filterProduct()
    {
        if(selectedTreeNode != null)
        {               
            try
            {
                CategoryEntity ce = (CategoryEntity)selectedTreeNode.getData();
                productEntities = productEntityControllerLocal.filterProductsByCategory(ce.getCategoryId());
            }
            catch(CategoryNotFoundException ex)
            {
                productEntities = productEntityControllerLocal.retrieveAllProducts();
            }
        }
        else
        {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
    }
    
    
    
    public void viewProductDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterProductsByCategory");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    private void createTreeNode(CategoryEntity categoryEntity, TreeNode parentTreeNode)
    {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);
                
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            createTreeNode(ce, treeNode);
        }
    }
    
    
    
    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode)
    {
        for(TreeNode tn:treeNode.getChildren())
        {
            CategoryEntity ce = (CategoryEntity)tn.getData();
            
            if(ce.getCategoryId().equals(selectedCategoryId))
            {
                return tn;
            }
            else
            {
                return searchTreeNode(selectedCategoryId, tn);
            }            
        }
        
        return null;
    }

    
    
    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }
    
    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) 
    {
        this.selectedTreeNode = selectedTreeNode;
        
        if(selectedTreeNode != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCategory", ((CategoryEntity)selectedTreeNode.getData()).getCategoryId());
        }
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
    
    public ProductEntity getSelectedProductEntityToView() {
        return selectedProductEntityToView;
    }

    public void setSelectedProductEntityToView(ProductEntity selectedProductEntityToView) {
        this.selectedProductEntityToView = selectedProductEntityToView;
    }    
}
