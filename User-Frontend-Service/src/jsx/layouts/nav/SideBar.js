import React, { Component } from "react";
import { Link } from "react-router-dom";
import PerfectScrollbar from "react-perfect-scrollbar";
import MetisMenu from "metismenujs";



class MM extends Component {
   componentDidMount() {
      this.$el = this.el;
      this.mm = new MetisMenu(this.$el);
   }
   componentWillUnmount() {
   }
   render() {
      return (
         <div className="mm-wrapper">
            <ul className="metismenu" ref={(el) => (this.el = el)}>
               {this.props.children}
            </ul>
         </div>
      );
   }
}

class SideBar extends Component {
   componentDidMount() {
      var btn = document.querySelector(".nav-control");
      var aaa = document.querySelector("#main-wrapper");

      function toggleFunc() {
         return aaa.classList.toggle("menu-toggle");
      }

      btn.addEventListener("click", toggleFunc);   
   }
   render() {
      let path = window.location.pathname;
      path = path.split("/");
      path = path[path.length - 1];

      let dashboard =[],
          training = [
             "",
            "personal-training-plans",
            "workout-statistic",
            "workout-center",
            "distance-map",
            "personal-record",
            "create-workout-wizard"
         ],
         profile = [
            "app-profile",
            "app-calender",
         ],

         community = [
            "community/training-plans"
         ];

      return (
         <div className="deznav">
            <PerfectScrollbar className="deznav-scroll">
               <MM className="metismenu" id="menu">
                  <li
                     className={`${
                        training.includes(path) ? "mm-active" : ""
                     }`}
                  >
					<Link className="has-arrow ai-icon" to="#">
               <i class="fa fa-address-book" aria-hidden="true"></i>
						<span className="nav-text">Twój Trening</span>
					</Link>
						<ul>
                  <li>
							   <Link className={`${ path === "workout-center" ? "mm-active" : ""}`} to="/workout-center">
									Twój trening
							   </Link>
							</li>
                     <li>
                        <Link className={`${ path === "personal-training-plans" ? "mm-active" : ""}`} to="/training-plans">
									Twoje plany treningowe
							   </Link>
                     </li>
                            <li>
                                <Link className={`${ path === "workout-statistic" ? "mm-active" : "" }`} to="/workout-statistic">
                                    Statystyki Treningu
                                </Link>
                            </li>
                            <li>
                                <Link className={`${ path === "create-workout-wizard" ? "mm-active" : "" }`} to="/create-workout-wizard">
                                    Kreator planu treningowego
                                </Link>
                            </li>
						</ul>
                  </li>
					<li className={`${profile.includes(path) ? "mm-active" : ""}`}>
						<Link className="has-arrow ai-icon" to="#">
                  <i class="fa fa-user" aria-hidden="true"></i>
							<span className="nav-text">Konto</span>
						</Link>
                     <ul>
                        <li>
                           <Link
                              className={`${
                                 path === "post-details" ? "mm-active" : ""
                              }`}
                              to="/profile"
                           >
                              Szczegóły konta
                           </Link>
                        </li>
   
                     </ul>
                  </li>

                  <li
                     className={`${
                        community.includes(path) ? "mm-active" : ""
                     }`}
                  >
					<Link className="has-arrow ai-icon" to="#">
               <i class="fa fa-comments" aria-hidden="true"></i>

						<span className="nav-text">Strefa społeczności</span>
					</Link>
						<ul>
                  <li>
							   <Link className={`${ path === "community/training-plans" ? "mm-active" : ""}`} to="community/training-plans">
									Publiczne plany treningowe
							   </Link>
							</li>
						</ul>
                  </li>

               </MM>
               <div className="copyright">
                  <p>
                     <strong>Workout-helper</strong>
                  </p>
               </div>
            </PerfectScrollbar>
         </div>
      );
   }
}

export default SideBar;
