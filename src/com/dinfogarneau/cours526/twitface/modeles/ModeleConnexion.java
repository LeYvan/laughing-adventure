package com.dinfogarneau.cours526.twitface.modeles;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.dinfogarneau.cours526.twitface.beans.ConnexionBean;
import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;

/**
 * Vérifie les données de logging
 * et enregistre les informations de connexion.
 * @author Yvan Dumont
 *
 */
public class ModeleConnexion {
	
	private ConnexionBean connexionBean;
	private String messageErreur;
	
	public ModeleConnexion(String nomUtilisateur, String motDePasse) {
		this.connexion(nomUtilisateur, motDePasse);
	}

	public ConnexionBean getConnexionBean() {
		return connexionBean;
	}

	public void setConnexionBean(ConnexionBean connexionBean) {
		this.connexionBean = connexionBean;
		this.messageErreur = null;
	}

	public String getMessageErreur() {
		return messageErreur;
	}

	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
		this.connexionBean = null;
	}
	
	/*
	 * Retourne vrai si les données sont valides.
	 */
	private Boolean valider(String nomUtilisateur, String motDePasse) {
		if (nomUtilisateur.trim().length() == 0 ||
				motDePasse.trim().length() == 0) {
			return false;
		}
		return true;
	}
	
	private Boolean utilisateurMembre(String nomUtilisateur, String motDePasse) {
		ReqPrepBdUtil req = new ReqPrepBdUtil("jdbc/twitface");

		String strSql = "SELECT MemNo, MemNom, MemNomUtil, MemMotPasse "
					  + "FROM membres "
					  + "WHERE MemNomUtil = ? "
					  + "AND MemMotPasse = SHA2(?,256)";
		
		try {
			req.ouvrirConnexion();
			req.preparerRequete(strSql, false);
			ResultSet rs = req.executerRequeteSelect(nomUtilisateur, motDePasse);
			
			if (rs.next()){
				this.connexionBean = new ConnexionBean();
				this.connexionBean.setNom		(rs.getString("MemNom"));
				this.connexionBean.setNomUtil	(rs.getString("MemNomUtil"));
				this.connexionBean.setNoUtil		(rs.getInt("MemNo"));
				this.connexionBean.setModeConn	(ConnexionMode.MEMBRE);
				return true;
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	
	private Boolean utilisateurAdmin(String nomUtilisateur, String motDePasse) {

		ReqPrepBdUtil req = new ReqPrepBdUtil("jdbc/twitface");

		String strSql = "SELECT AdminNO, AdminNom, AdminNomUtil, AdminMotPasse "
					  + "FROM administrateurs "
					  + "WHERE AdminNomUtil = ? "
					  + "AND AdminMotPasse = SHA2(?,256) ";
		
		try {
			req.ouvrirConnexion();
			req.preparerRequete(strSql, false);
			ResultSet rs = req.executerRequeteSelect(nomUtilisateur, motDePasse);
			
			if (rs.next()){
				this.connexionBean = new ConnexionBean();
				this.connexionBean.setNom		(rs.getString(2));
				this.connexionBean.setNomUtil	(rs.getString(3));
				this.connexionBean.setNoUtil		(rs.getInt(1));
				this.connexionBean.setModeConn	(ConnexionMode.ADMIN);
				return true;
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	
	private Boolean authentifier(String nomUtilisateur, String motDePasse)
	{
		return this.utilisateurMembre(nomUtilisateur , motDePasse) || 
			   this.utilisateurAdmin(nomUtilisateur, motDePasse);
	}
	
	public void connexion(String nomUtilisateur, String motDePasse) {
		
		// Validation
		if (!valider(nomUtilisateur, motDePasse)) {
			this.setMessageErreur("Paramètre de connexion non fournis.");
			return;
		}
		
		// Connexion
		if (!this.authentifier(nomUtilisateur, motDePasse))
		{
			this.setMessageErreur("Paramètre de connexion invalides.");
		}
	}
}
