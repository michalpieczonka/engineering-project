import React, {useEffect, useState, Fragment} from 'react'
import { Button } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import { getCurrentTrainingPlan,  getAllPublicTrainingPlans} from '../../../../services/TrainingPlanService';
import swal from "sweetalert";
import { mapPlanPriority } from '../../workout-create/CommonMappers';

import dumbells_icon_s from "../../../../images/plans/dumbells_icon_s.png";

import dumbells from "../../../../images/plans/dumbells.png";

/// Scroll
import PerfectScrollbar from "react-perfect-scrollbar";
import { store } from '../../../../store/store';
import PageTitle from '../../../layouts/PageTitle';

const PublicTrainingPlans = () => {

   const state = store.getState();
   let userId = state.auth.auth.userId;
    
   const [trainingPlans, setTrainingPlans] = useState([])
   const [currentTrainingPlan, setCurrentTrainingPlan] = useState('')
   const [hasCurrent, setHasCurrent] = useState(false)
   const[refresh, setRefresh] = useState(1)


   useEffect(() => { 
      getAllPublicTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push(
            {id: x.id,
               title: x.title,
               description: x.description, 
               numberOfTrainingDays: x.numberOfTrainingDays,
                planPriority: x.planPriority, 
                isPublic: x.public, 
                createdByRequestedUser: x.createdByRequestedUser,
                inUserPlans: x.inUserPlans,
                creationDate: new Date(x.creationDate).toLocaleDateString(),
                planUsersIds: x.planUsersIds,
                planRates: x.planRates.map(rate => ({id: rate.id, description: rate.description, rate: rate.rate})),
                averageRate: x.averageRate,
                
               }
            ))
         setTrainingPlans(tmp)
         console.log(tmp)
      }).catch(error => swal("Błąd", "Nie udało się załadować publicznych planów treningowych", "error"))
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
     }, []); 
     
   const [activesigleMenu, setActivesigleMenu] = useState(false);
   const history = useHistory()
   const pushToDetails = (planId) => {
      history.push(`training-plan/${planId}`)
   }

   return (
      <Fragment>
            <PageTitle activeMenu="Publiczne plany treningowe" motherMenu="Strefa społeczności" />
         <div className="row">
            <div className="col-xl-9 col-xxl-8">
               <div className="row">
                  <div className="col-xl-12">
                     <div className="card">
                        <div className="card-header d-sm-flex d-block pb-0 border-0">
                           <div className="mr-auto pr-3 mb-sm-0 mb-3">
                              <h3 className="text-black fs-20"><center>Publiczne plany treningowe</center></h3>
                              <p className="fs-13 mb-2 mb-sm-0 text-black">
                           Poniżej znajdziesz wszystkie plany treningowe udostępnione przez innych użytkowników aplikacji. < br/>
                           Możesz z nich skorzystać w celu ułożenia swojego indywidualnego planu lub dodać je do własnych planów treningowych. < br/>
                           Zachęcamy również do udostępniania własnych planów treningowych, aby pomóc innym użytkownikom aplikacji. < br/>
                        </p>
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
                                       {
                                          !d.inUserPlans &&
                                          <span>  {d.description}  </span>
                                       }
                                     
                                       {
                                          d.inUserPlans &&    <span style={{color: "red"}}> Plan treningowy należy do twoich planów treningowych</span>
                                       } 
                                       <br />
                                       <i className="fa fa-user scale5 mr-3" />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Liczba użytkowników: {d.planUsersIds.length}
                                       </span>< br/>
                                       <i className="fa fa-bar-chart scale5 mr-3" />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Liczba opinii: {d.planRates.length}
                                       </span> < br/>
                                       <i className="fa fa-star scale5 mr-3" />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Średnia ocena: {d.averageRate}
                                       </span> < br/>
                                    
                                       <i className="fa fa-calendar scale5 mr-3" />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Data utworzenia: {d.creationDate}
                                       </span> < br/>
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
                                 <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="primary btn-sm" onClick={() => pushToDetails(d.id)}>
                               Wyświetl szczegóły
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
                                 Twój aktualnie wykonywany plan treningowy
                              </h4>
                           </div>

                        </div>

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
                  
                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary">
                           <Link to={"/profile"}> Ustaw plan treningowy</Link>
                        </Button>
                        </div>}

                     </div>
                  </div>
      
      
               </div>
               
            </div>
            
         </div>
      </Fragment>
   );
};

export default PublicTrainingPlans;
