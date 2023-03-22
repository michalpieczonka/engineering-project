import React from "react";

import { Link } from "react-router-dom";
/// Scroll
import PerfectScrollbar from "react-perfect-scrollbar";

import LogoutPage from './Logout';
/// Image
import user_icon from "../../../images/plans/user_icon.png";
import { Dropdown } from "react-bootstrap";

const Header = ({ toggle, onProfile, onNotification, onClick }) => {
  var path = window.location.pathname.split("/");
  var name = path[path.length - 1].split("-");
  var filterName = name.length >= 3 ? name.filter((n, i) => i > 0) : name;
  var finalName = filterName.includes("app")
    ? filterName.filter((f) => f !== "app")
    : filterName.includes("ui")
    ? filterName.filter((f) => f !== "ui")
    : filterName.includes("uc")
    ? filterName.filter((f) => f !== "uc")
    : filterName.includes("basic")
    ? filterName.filter((f) => f !== "basic")
    : filterName.includes("jquery")
    ? filterName.filter((f) => f !== "jquery")
    : filterName.includes("table")
    ? filterName.filter((f) => f !== "table")
    : filterName.includes("page")
    ? filterName.filter((f) => f !== "page")
    : filterName.includes("ecom")
    ? filterName.filter((f) => f !== "ecom")
    : filterName.includes("chart")
    ? filterName.filter((f) => f !== "chart")
    : filterName.includes("editor")
    ? filterName.filter((f) => f !== "editor")
    : filterName;
  return (
    <div className="header">
		<div className="header-content">
			<nav className="navbar navbar-expand">
				<div className="collapse navbar-collapse justify-content-between">
					<div className="header-left">
				
					</div> 	
					<ul className="navbar-nav header-right">
				
						<Dropdown as="li" className="nav-item header-profile ">
							<Dropdown.Toggle as="a" to="#" variant="" className="nav-link i-false c-pointer">								
								<img src={user_icon} width="20" alt=""/>
									{}
                                
							</Dropdown.Toggle>
							<Dropdown.Menu align="right" className="mt-2">
							  <Link to="/profile" className="dropdown-item ai-icon">
							  <i class="fa fa-user-circle-o" aria-hidden="true"></i>
								<span className="ml-2">Profil </span>
							  </Link>
                               <LogoutPage />
							</Dropdown.Menu>
						</Dropdown>
					</ul>
				</div>
			</nav>
		</div>
    </div>
  );
};

export default Header;
