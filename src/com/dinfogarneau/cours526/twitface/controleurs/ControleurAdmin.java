package com.dinfogarneau.cours526.twitface.controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;

public class ControleurAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Attributs
	// =========
	/**
	 * URI sans le context path.
	 */
	protected String uri;
	
	/**
	 * Vue à afficher (chemin du fichier sur le serveur).
	 */
	protected String vue;
	
	/**
	 * Fragment de la vue (chemin du fichier sur le serveur) à charger
	 * dans la zone de contenu si la vue est créée à partir du gabarit.
	 */
	protected String vueContenu;
	
	/**
	 * Sous-titre de la vue si la vue est créée à partir du gabarit.
	 */
	protected String vueSousTitre;
	
	protected boolean preTraitement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Récupération de l'URI sans le context path.
		this.uri = request.getRequestURI().substring(request.getContextPath().length());
		
		// Expiration de la cache pour les pages de cette section.		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  // HTTP 1.1.
		response.setHeader("Pragma", "no-cache");  // HTTP 1.0.
		response.setDateHeader("Expires", 0);  // Proxies.
		
		// Récupération du mode de connexion dans la session utilisateur.
		// *** À MODIFIER (UTILISATION DU BEAN DE CONNEXION) ***
		ConnexionMode modeConn = (ConnexionMode) request.getSession().getAttribute("modeConn");

		// Contrôle d'accès à la section pour les clients.
		if (modeConn == null || modeConn != ConnexionMode.ADMIN) {
			// Non connecté en tant qu'admin; on retourne une code d'erreur
			// HTTP 401 qui sera intercepté par la page d'erreur "erreur-401.jsp".
			response.sendError(401);
			return false;
		}
		else
			return true;
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(preTraitement(request, response)){ 
		
			if (uri.equals("/admin")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/admin/accueil-admin.jsp";
			}
			if (uri.equals("/admin/supp-pub")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/en-construction.jsp";
			}
			
		}
		
		postTraitement(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void postTraitement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Doit-on transférer le contrôle vers une vue ?
		if (this.vue != null) {
			// Doit-on conserver les informations pour la production d'une vue à partir du gabarit ?
			if (this.vueContenu != null || this.vueSousTitre != null) {
				// On conserve le chemin du fichier du fragment de la vue ainsi que le
				// sous-titre de la vue dans les attributs de la requête;
				// ces informations serviront à générer la vue à partir du gabarit.
				request.setAttribute("vueContenu", this.vueContenu);
				request.setAttribute("vueSousTitre", this.vueSousTitre);
			}
			// Transfert du contrôle de l'exécution à la vue.
			request.getRequestDispatcher(this.vue).forward(request, response);
		}		
	}	
}
