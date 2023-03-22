import React, {useEffect, useState, Fragment} from 'react'
import { Button, Modal, Tab, Tabs, Badge } from "react-bootstrap";
import { useDispatch, useSelector } from 'react-redux';
import { Link } from "react-router-dom";
import { getAllUserTrainingPlans, getCurrentTrainingPlan, setCurrentUserTrianingPlan, getAllPublicTrainingPlans } from '../../../services/TrainingPlanService';
import { mapPlanPriority } from '../workout-create/CommonMappers';
import swal from "sweetalert";
import Select from "react-select";

import dumbells_icon_s from "../../../images/plans/dumbells_icon_s.png";

import dumbells from "../../../images/plans/dumbells.png";

/// Scroll
import PerfectScrollbar from "react-perfect-scrollbar";
import { store } from '../../../store/store';
import PageTitle from '../../layouts/PageTitle';


const PersonalTrainingPlans = () => {

   const state = store.getState();
   let userId = state.auth.auth.userId;
    
   const [trainingPlans, setTrainingPlans] = useState([])
   const [selectedTrainingPlan, setSelectedTrainingPlan] = useState('')
   const [currentTrainingPlan, setCurrentTrainingPlan] = useState('')
   const [hasCurrent, setHasCurrent] = useState(false)
   const [currentNotDefinedMessage, setCurrentNotDefinedMessage] = useState('Brak aktualnego planu treningowego')
   const [currentUpdatedPlan, setCurrentUpdatedPlan] = useState('')
   const [showChangePlanModal, setShowChangePlanModal] = useState(false);
   const[refresh, setRefresh] = useState(1)


   const handleChangeCurrentTrainingPlan = e => {
      let tmp = {value: e.value, label: e.label}
      setCurrentUpdatedPlan(tmp)
      console.log(currentUpdatedPlan)
   }

   const updateCurrentTrainingPlan = () => {
      setShowChangePlanModal(false)
      setCurrentUserTrianingPlan(userId, currentUpdatedPlan.value)
      .then(response => {
         if(response.status === 200){
            swal('Sukces', 'Aktualny plan treningowy został zmieniony !', "success");
            setRefresh((prevState) => prevState+1)
         } else {
            swal('Błąd', 'Nie udało się zmienić aktualnie wykonywanego planu treningowego, wystąpił błąd.', "error");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie zmiany planu treningowego.', "error");
         console.log(error)})
   }



   useEffect(() => { 
      getAllUserTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push({id: x.id, title: x.title, description: x.description, numberOfTrainingDays: x.numberOfTrainingDays, planPriority: x.planPriority, isPublic: x.isPublic, planType: x.planType}))
         setTrainingPlans(tmp)
      }).catch(error => swal("Błąd", "Nie udało się załadować Twoich planów treningowych", "error"))
     }, [refresh]);

     useEffect(() => { 
      getCurrentTrainingPlan(userId).then((response) => {
         let tmp = response.data
         if(tmp.id != 0){
            setHasCurrent(true)
            let plan = {
               id: tmp.id,
               title: tmp.title,
               description: tmp.planDescription,
               numberOfTrainingDays: tmp.numberOfTrainingDays,
               planPriority: tmp.planPriority,
               planType: tmp.planType,
            }
            setCurrentTrainingPlan(plan)
         }
      }).catch(error => swal("Błąd", "Nie udało się załadować Szczegółów twojego planu treningowego", "error"))
     }, [refresh]); 
     
   const [activesigleMenu, setActivesigleMenu] = useState(false);

   const [publicPlans, setPublicPlans]  = useState('empty')
   useEffect(() => { 
      getAllPublicTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push(
            {id: x.id,
               title: x.title,
                isPublic: x.public, 
                createdByRequestedUser: x.createdByRequestedUser,
                inUserPlans: x.inUserPlans,
                averageRate: x.averageRate,
                
               }
            ))
            const result = tmp.filter(x => (x.createdByRequestedUser === true && x.isPublic === true))
            .sort((a, b) => (a.averageRate > b.averageRate ? 1 : -1))
            .slice(0, 3);
         setPublicPlans(result)
      }).catch(error => swal("Błąd", "Nie udało się załadować publicznych planów treningowych", "error"))
     }, [refresh]);

   return (
      <Fragment>
           <PageTitle activeMenu="Twoje plany  treningowe" motherMenu="Twoj trening" />
         <div className="row">
            <div className="col-xl-9 col-xxl-8">
               <div className="row">
                  <div className="col-xl-12">
                     <div className="card">
                        <div className="card-header d-sm-flex d-block pb-0 border-0">
                           <div className="mr-auto pr-3 mb-sm-0 mb-3">
                              <h4 className="text-black fs-20"><center>Twoje plany treningowe</center></h4>
                           </div>
      
                        </div>
                        <PerfectScrollbar
                           className="card-body loadmore-content pb-4 dz-scroll height750"
                           id="DietMenusContent"
                        >
                           {trainingPlans.map((d, i) => (
                              <div
                                 className="media mb-3 pb-3 d-md-flex d-block menu-list"
                                 key={i}
                              >
   
                               
                                    <img
                                       className="rounded mr-3 mb-md-0 mb-3"
                                       src={dumbells}
                                       alt=""
                                       width={130}
                                    />
                                
                                 <div className="media-body col-lg-6 pl-0">

                                    <h6 className="fs-16 font-w600">
                     
                                          {d.title}
                                    
                                    </h6>
                                    <p className="fs-14 mb-md-0 mb-3">
                                       {d.description}
                                    </p>
                                    
                                 </div>
                                 <ul className="m-md-auto mt-2 pr-4 mb-2">
                                    <li className="mb-2 text-nowrap">
                                    <i class="fa fa-list-ol" aria-hidden="true"></i>
                                       <span className="fs-14 text-black text-nowrap font-w500">
                                       Dni treningowych {d.numberOfTrainingDays} 
                                       </span>
                                    </li>
                                    <li className="mb-2 text-nowrap">
                                    <i class="fa fa-hand-o-right" aria-hidden="true"></i>
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Typ planu: {d.planType} 
                                       </span>
                                    </li>
                                    <li className="text-nowrap">
                                       <i
                                          className="fa fa-star-o mr-3 scale5 text-warning"
                                          aria-hidden="true"
                                       />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Priorytet: {mapPlanPriority(d.planPriority)} 
                                       </span>
                                    </li>
                                 </ul>
                                 <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary">
                                <Link to={"/training-plan/"+d.id}> Wyświetl szczegóły</Link>
                        </Button>
                              </div>
                           ))}
                        </PerfectScrollbar>

                     </div>
                  </div>
               </div>
            </div>
            <div className="col-xl-3 col-xxl-4">
               <div className="row">
                  <div className="col-xl-12 col-lg-6">
                     <div className="card">
                        <div className="card-header pb-0 border-0">
                           <div className="mr-auto pr-3">
                              <h4 className="text-black fs-20">
                                 Aktualnie wykonywany plan treningowy
                              </h4>
                           </div>

                        </div>
                        <Modal className="fade" show={showChangePlanModal}>
                           <Modal.Header>
                              <Modal.Title>Zmiana aktualnego planu treningowego</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setShowChangePlanModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
          <h3> Wybierz nowy plan treningowy </h3>
          <label className="text-label">Aktualnie wykonywany plan treningowy: <br /> <b>Id: {currentTrainingPlan.id}</b>, Tytuł: <b>{currentTrainingPlan.title}</b> </label>
          
          <Select                 
                        onChange={e => handleChangeCurrentTrainingPlan(e)}
                        value = {currentUpdatedPlan}
                        options={trainingPlans.map(plan => ({value: plan.id, label: plan.title}))}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setShowChangePlanModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => updateCurrentTrainingPlan()} variant="primary">Zapisz zmiany</Button>
                           </Modal.Footer>
                        </Modal>

                       {hasCurrent && <div className="card-body">
                           <div className="media mb-3">
                              <Link to={"/training-plan/"+currentTrainingPlan.id}>
                                 <img
                                    src={dumbells_icon_s}
                                    alt=""
                                    className="rounded mr-3"
                                    width={86}
                                 />
                              </Link>
                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
                                    <Link
                                       to={"/training-plan/"+currentTrainingPlan.id}
                                       className="text-black"
                                    >
                                       {currentTrainingPlan.title}
                                    </Link>
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                           <ul className="m-md-auto mt-2 pr-4">
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-list-ol" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Dni treningowych {currentTrainingPlan.numberOfTrainingDays}
                                 </span>
                              </li>
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-hand-o-right" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Typ planu {currentTrainingPlan.planType}
                                 </span>
                              </li>
                                              <li className="text-nowrap">
                                       <i
                                          className="fa fa-star-o mr-3 scale5 text-warning"
                                          aria-hidden="true"
                                       />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Priorytet: {mapPlanPriority(currentTrainingPlan.planPriority)} 
                                       </span>
                                    </li>

                           </ul>
                           <br />

                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="primary">
                           <Link to={"/training-plan/"+currentTrainingPlan.id}> Wyświetl szczegóły</Link>
                        </Button>
                        < br />
                             <Button
                           variant="primary"
                           className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn"
                           onClick={() => setShowChangePlanModal(true)}
                        >
                           Zmień plan
                        </Button>
                        </div>}

                        {!hasCurrent && <div className="card-body">
                           <div className="media mb-3">

                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
         
                                       Nie posiadasz aktualnie zdefiniowanego planu treiningowego
                             
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                  
                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" 
                           onClick={() => setShowChangePlanModal(true)}>
                          Ustaw plan treningowy
                        </Button>
                        </div>}

                     </div>
                  </div>
                  <div className="col-xl-12 col-lg-6">
                     <div className="card">
                        <div className="card-header d-sm-flex d-block border-0 pb-4">
                           <div className="mr-auto pr-3">
                              <h4 className="text-black fs-20">
                                 Najlepiej oceniane 
                              </h4>
                              <p className="fs-13 mb-0">
                                 Twoje publiczne plany treningowe, które uzyskały najlepsze oceny
                              </p>
                           </div>
                        </div>
                        { publicPlans !== 'empty' &&
        <PerfectScrollbar
        className="card-body loadmore-content dz-scroll pb-0 pt-0 height250"
        id="TrendingIngridientsContent"
     >
        {
           publicPlans.map((plan, index) => {
              return (
                 <div className="media border-bottom py-3">
                 <Link to={"/training-plan/"+plan.id}>
                    <img
                       src={dumbells_icon_s}
                       alt=""
                       className="rounded mr-3"
                       width={50}
                    />
                 </Link>
                 <div className="pr-3 mr-auto media-body">
                    <h6 className="fs-16 font-w600 mb-0">
                       <Link
                          to="/training-plan/"
                          className="text-black"
                       >
                         {plan.title}
                       </Link>
                    </h6>
                    <span className="fs-12">{plan.description}</span>
                 </div>
                 <div className="text-center">
                    <span className="d-block fs-16 text-black font-w600">
                    <i className="fa fa-star" /> &nbsp;
                    {Math.floor((plan.averageRate)*100)/100} / 10
                    </span>
                 </div>
              </div>
              );
           })
        }

     </PerfectScrollbar>
                        }
                

                     </div>
                  </div>
               </div>
            </div>
         </div>
      </Fragment>
   );
};

export default PersonalTrainingPlans;
