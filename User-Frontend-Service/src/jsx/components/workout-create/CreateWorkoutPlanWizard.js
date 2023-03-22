import {React, Fragment, useState } from "react";

import Multistep from "react-multistep";

import StepOne from "./PlanConfigurationStep";
import StepTwo from "./PlanDetailsStep";

const Wizard = () => {


   const steps = [
      { name: "Training plan details", component: <StepOne /> },

      { name: "Training plan exercises", component: <StepTwo /> },
   ];

   const prevStyle = {
      background: "#52B141",
      borderWidth: "0px",
      color: "#fff",
      borderRadius: "4px",
      fontSize: "14px",
      fontWeight: "600",
      padding: "0.55em 2em",
      border: "1px solid #EEEEEE",
      marginRight: "1rem",
      text: "ez"
   };
   const nextStyle = {
      background: "#52B141",
      borderWidth: "0px",
      color: "#fff",
      borderRadius: "4px",
      fontSize: "14px",
      fontWeight: "600",
      padding: "0.55em 2em",
   };
   return (
      <Fragment>

         <div className="row">
            <div className="col-xl-12 col-xxl-12">
               <div className="card">
                  <div className="card-header">
                     <h4 className="card-title">Kreator planu treningowego</h4>
                  </div>
                  <div className="card-body">
                     <form
                        onSubmit={(e) => e.preventDefault()}
                        id="step-form-horizontal"
                        className="step-form-horizontal"
                     >
                        <Multistep
                           showNavigation={true}
                           steps={steps}
                           prevStyle={prevStyle}
                           nextStyle={nextStyle}          
                        />
                     </form>
                  </div>
           
               </div>

            </div>
         </div>
      </Fragment>
   );
};

export default Wizard;
