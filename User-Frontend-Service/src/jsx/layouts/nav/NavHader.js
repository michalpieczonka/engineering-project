import React, { useState } from "react";

/// React router dom
import { Link } from "react-router-dom";

/// images
import logoText from "../../../images/logo-text.png";

const NavHader = () => {
   const [toggle, setToggle] = useState(false);
   return (
      <div className="nav-header">
         <Link to="/" className="brand-logo">
            <img className="brand-title" src={logoText} alt="" />
         </Link>

         <div className="nav-control" onClick={() => setToggle(!toggle)}>
            <div className={`hamburger ${toggle ? "is-active" : ""}`}>
            </div>
         </div>
      </div>
   );
};

export default NavHader;
