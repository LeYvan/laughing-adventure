<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

    <div id="listePub">
    	<ul>
    		<sql:query var="listePub" dataSource="jdbc/twitface">
	    		SELECT PubNo, PubTexte, PubDate, MemNom
	    		FROM publications INNER JOIN membres ON publications.MemNoCreateur = membres.MemNo
	    		ORDER BY PubDate;
    		</sql:query>
    		
    		<c:choose>
    			<c:when test="${empty listePub.rows}">
    				<p>
    					Aucune publications.
    				</p>
    			</c:when>
    			<c:otherwise>
    				<h2>Publications</h2>
    				
    				<c:forEach var="pub" items="${listePub.rows}">
    					<li>
    						<ul>
	    						<li>
	    							${pub.PubDate}
	    						</li>
	    						<li>
	    							${pub.MemNom}
	    						</li>
	    						<li>
	    							${pub.PubTexte}
	    						</li>
  								<li>
  									<a href="${pageContext.request.contextPath}/admin/supp-pub?no-pub=${pub.PubNo}">Supprimer</a>
  								</li>
    						</ul>
    					</li>
    				</c:forEach>
    			</c:otherwise>
    		</c:choose>
    	</ul>
    </div>