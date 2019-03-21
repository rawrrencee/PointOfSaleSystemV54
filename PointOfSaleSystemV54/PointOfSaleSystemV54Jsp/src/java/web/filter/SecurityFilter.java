package web.filter;

import entity.StaffEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;



@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})

public class SecurityFilter implements Filter
{    
    FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/PointOfSaleSystemV54Jsp";
    
   

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }



    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();
        String requestQueryString = httpServletRequest.getQueryString();
        
        

        if(httpSession.getAttribute("isLogin") == null)
        {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean)httpSession.getAttribute("isLogin");
        
        
        
        if(!excludeLoginCheck(requestServletPath, requestQueryString))
        {
            if(isLogin == true)
            {
                StaffEntity currentStaffEntity = (StaffEntity)httpSession.getAttribute("currentStaffEntity");
                
                if(checkAccessRight(requestServletPath, requestQueryString, currentStaffEntity.getAccessRightEnum()))
                {
                    chain.doFilter(request, response);
                }
                else
                {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/Controller?action=accessRightError");
                }
            }
            else
            {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/Controller?action=accessRightError");
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }



    public void destroy()
    {

    }
    
    
    
    private Boolean checkAccessRight(String path, String queryString, AccessRightEnum accessRight)
    {        
        if(accessRight.equals(AccessRightEnum.CASHIER))
        {
            if(path.equals("/Controller"))
            {
                if(queryString.startsWith("action=checkout") ||
                    queryString.startsWith("action=voidRefund") ||
                    queryString.startsWith("action=viewMySaleTransactions"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            
            if(path.equals("/cashierOperation/checkout.jsp") ||
                path.equals("/cashierOperation/voidRefund.jsp") ||
                path.equals("/cashierOperation/viewMySaleTransactions.jsp"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(accessRight.equals(AccessRightEnum.MANAGER))
        {
            if(path.equals("/Controller"))
            {
                if(queryString.startsWith("action=checkout") ||
                    queryString.startsWith("action=voidRefund") ||
                    queryString.startsWith("action=viewMySaleTransactions") ||
                    queryString.startsWith("action=createNewStaff") ||
                    queryString.startsWith("action=viewStaffDetails") ||
                    queryString.startsWith("action=updateStaff") ||
                    queryString.startsWith("action=deleteStaff") ||
                    queryString.startsWith("action=viewAllStaffs") ||
                    queryString.startsWith("action=createNewProduct") ||
                    queryString.startsWith("action=viewProductDetails") ||
                    queryString.startsWith("action=updateProduct") ||
                    queryString.startsWith("action=deleteProduct") ||
                    queryString.startsWith("action=viewAllProducts"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            
            if(path.equals("/cashierOperation/checkout.jsp") ||
                path.equals("/cashierOperation/voidRefund.jsp") ||
                path.equals("/cashierOperation/viewMySaleTransactions.jsp") ||
                path.equals("/systemAdministration/createNewStaff.jsp") ||
                path.equals("/systemAdministration/viewStaffDetails.jsp") ||
                path.equals("/systemAdministration/viewAllStaffs.jsp") ||
                path.equals("/systemAdministration/createNewProduct.jsp") ||
                path.equals("/systemAdministration/viewProductDetails.jsp") ||
                path.equals("/systemAdministration/viewAllProducts.jsp"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        return false;
    }



    private Boolean excludeLoginCheck(String path, String queryString)
    {
        if(path.equals("/Controller"))
        {
            if(queryString.startsWith("action=accessRightError") ||
                queryString.startsWith("action=login") ||
                queryString.startsWith("action=logout"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        if(path.equals("/index.jsp") ||
            path.equals("/accessRightError.jsp") ||
            path.startsWith("/images") ||
            path.startsWith("/css"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
