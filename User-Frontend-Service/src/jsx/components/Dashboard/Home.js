import React, {useEffect, useState} from 'react'
import { Link, useHistory } from "react-router-dom";

import loadable from "@loadable/component";
import pMinDelay from "p-min-delay";

import Slider from "react-slick";

import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import dumbells_icon_s from "../../../images/plans/dumbells_icon_s.png";


import { getUserWorkoutStatistics } from "../../../services/UserDetailsService";
import { getCurrentTrainingPlan, getAllUserTrainingPlans, getAllPublicTrainingPlans } from "../../../services/TrainingPlanService";
import { store } from "../../../store/store";
import { Tab, Button} from "react-bootstrap";
import { mapPlanPriority } from '../workout-create/CommonMappers';

const BasicStatsChart = loadable(() => pMinDelay(import("../workout-statistics/BasicBar"), 50));




const Home = () => {
   function SNext(props) {
      const { onClick } = props;
      return (
         <div className="owl-next" onClick={onClick} style={{ zIndex: 99 }}>
            <i className="fa fa-caret-right" />
         </div>
      );
   }

   function SPrev(props) {
      const { onClick } = props;
      return (
         <div
            className="owl-prev disabled"
            onClick={onClick}
            style={{ zIndex: 99 }}
         >
            <i className="fa fa-caret-left" />
         </div>
      );
   }

   const settings = {
      focusOnSelect: true,
      infinite: true,
      slidesToShow: 2,
      slidesToScroll: 1,
      speed: 500,
      nextArrow: <SNext />,
      prevArrow: <SPrev />,
      responsive: [
         {
            breakpoint: 1599,
            settings: {
               slidesToShow: 2,
               slidesToScroll: 1,
            },
         },
         {
            breakpoint: 990,
            settings: {
               slidesToShow: 1,
               slidesToScroll: 1,
            },
         },
      ],
   };

   const history = useHistory()
   const state = store.getState();
   let userId = state.auth.auth.userId;

   const[statistics, setStatistics] = useState({})
   const[workoutsPerPlan, setWorkoutsPerPlan] = useState('empty')
   const[plansLoading, setPlansLoading] = useState(true)


   useEffect(() => { 
      if(plansLoading === false) {
         getUserWorkoutStatistics(userId).then((response) => {
            let tmp = response.data
            let stats = {
               totalWorkouts: tmp.totalWorkouts,
               totalTrainingPlans: tmp.totalTrainingPlans,
               totalTrainingMinutes: tmp.totalTrainingMinutes,
               averageTrainingTime: tmp.averageTrainingTime,
               averageTrainingRate: tmp.averageTrainingRate
            }
            setStatistics(tmp)
   
            let perPlan = tmp.workoutsPerPlan.map(x => ({trainingPlanId: x.trainingPlanId, totalWorkouts: x.totalWorkouts, planName: trainingPlans.find(p => p.id == x.trainingPlanId).title }))
            setWorkoutsPerPlan(perPlan)
      }


      ).catch(error => swal("Błąd", "Nie udało się załadować statystyk", "error"))
     } }, [plansLoading]);

     const[trainingPlans, setTrainingPlans] = useState([])

   useEffect(() => { 
      getAllUserTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push({id: x.id, title: x.title}))
         setTrainingPlans(tmp)
         setPlansLoading(false)
      }).catch(error => swal("Błąd", "Nie udało się załadować Twoich planów treningowych", "error"))
     }, []);


     const [currentTrainingPlan, setCurrentTrainingPlan] = useState({})
     const [hasCurrent, setHasCurrent] = useState(false)

     useEffect(() => { 
      getCurrentTrainingPlan(userId).then((response) => {
         let tmp = response.data
         if(tmp.id != 0){
            let plan = {
               id: tmp.id,
               title: tmp.title,
               description: tmp.planDescription,
               numberOfTrainingDays: tmp.numberOfTrainingDays,
               planPriority: mapPlanPriority(tmp.planPriority),
               planType: tmp.planType
            }
            setCurrentTrainingPlan(plan)
            setHasCurrent(true)
         }
      }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów twojego planu treningowego", "error"))
     }, []); 

     const [publicPlans, setPublicPlans] = useState('empty')
     useEffect(() => { 
      getAllPublicTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push(
            {id: 
               x.id,
               title: x.title,
               description: x.description, 
               numberOfTrainingDays: x.numberOfTrainingDays,
                planPriority: x.planPriority, 
                inUserPlans: x.inUserPlans,
                creationDate: new Date(x.creationDate).toLocaleDateString(),
                planUsersIds: x.planUsersIds,
                planRates: x.planRates.map(rate => ({id: rate.id, description: rate.description, rate: rate.rate})),
                averageRate: x.averageRate,
               }
            ))

         let tmp2 = tmp.sort((a, b) => (a.averageRate > b.averageRate ? 1 : -1))
         .slice(0, 4);

         setPublicPlans(tmp2)
         console.log(tmp2)
      }).catch(error => swal("Błąd", "Nie udało się załadować publicznych planów treningowych", "error"))
     }, []);

   return (
      <>
         <div className="row">
            {!hasCurrent & <div className="card-header d-sm-flex d-block pb-0 border-0">
                        <div className="mr-auto pr-3">
                           <h4 className="text-black fs-20">
                             Nie posiadasz żadnych planów treningowych
                           </h4>
                           <p className="fs-13 mb-0 text-black">
                             Aby zobaczyć statystyki utwórz plan treningowy i wykonaj sesję treningową. Powodzenia !
                           </p>
                        </div>
                        <Link
                           to="/create-workout-wizard"
                           className="btn btn-primary rounded d-none d-lg-block ml-0 ml-md-5"
                        >
                           Utwórz plan treningowy
                        </Link>
                     </div> }

         {hasCurrent && <div className="col-xl col-md-6">
               <div className="card">
                  <div className="card-body p-4">
   <center>
                     <h2 className="fs-64 text-black font-w600 mb-100">{statistics.totalWorkouts}</h2>
                     <span className="fs-14">Wszystkich odbytych treningów</span>
                     </center>  </div>
               </div>
            </div> }
            {hasCurrent && <div className="col-xl col-md-6 col-sm-6">
               <div className="card">
                  <div className="card-body p-4">
     <center>
                     <h2 className="fs-64 text-black font-w600 mb-100">{statistics.totalTrainingPlans}</h2>
                     <span className="fs-14">Wszystkich planów treningowych</span></center>
                  </div>
               </div>
            </div> }
            {hasCurrent &&  <div className="col-xl col-md-4 col-sm-6">
               <div className="card">
                  <div className="card-body p-4">
                   <center>
                     <h2 className="fs-64 text-black font-w600 mb-100">{statistics.totalTrainingMinutes}</h2>
                     <span className="fs-14">Łacznie minut treningu</span></center>
                  </div>
               </div>
            </div> }
            {hasCurrent &&  <div className="col-xl col-md-4 col-sm-6">
               <div className="card">
                  <div className="card-body p-4">
                   <center>
                     <h2 className="fs-64 text-black font-w600 mb-100">
                        {statistics.averageTrainingTime}
                     </h2>
                     <span className="fs-14">Średni czas [min] pojedynczej sesji treningowej</span></center>
                  </div>
               </div>
            </div> }

            {hasCurrent && <div className="col-xl col-md-4 col-sm-6">
               <div className="card">
                  <div className="card-body p-4">
                    <center>
                     <h2 className="fs-64 text-black font-w600 mb-100">
                       {Math.floor((statistics.averageTrainingRate)*100)/100}
                     </h2>
                     <span className="fs-14">Średnia subiektywna ocena odbytych sesji treningowych</span></center>
                  </div>
               </div>
            </div> }

            <div className="col-xl-9 col-xxl-8">
               <div className="card">
                  <div className="card-header flex-wrap pb-0 border-0">
                     <div className="mr-auto pr-3 mb-2">
                        <h4 className="text-black fs-20">Historia twoich planów treningowych</h4>
                        <p className="fs-13 mb-2 mb-sm-0 text-black">
                           Zestawienie liczby wykonanych sesji treningowych / plan treningowy.
                        </p>
                     </div>

                  </div>
                  <div className="card-body pt-3">
                  {workoutsPerPlan !== 'empty' &&  <BasicStatsChart xdata={workoutsPerPlan.map(x => x.totalWorkouts)} ydata={workoutsPerPlan.map(x  =>  x.planName)} text={'Liczba sesji treningowych'} />}
                  </div>
               </div>
            </div>
            <div className="col-xl-3 col-xxl-4 col-md-6">
               <div className="card">
                  <div className="card-header border-0 pb-0">
                     <h4 className="text-black fs-20 mb-0">Aktualnie wykonywany plan treningowy</h4>
                  </div>
                  { hasCurrent &&<div className="card-body text-center">
              
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
                              <i class="fa fa-hand-o-right mr-3 scale5" aria-hidden="true"></i>

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
                                          Priorytet: {currentTrainingPlan.planPriority} 
                                       </span>
                                    </li>

                           </ul>
                           <br />

                           <Button className="btn btn-primary rounded " variant="primary">
                           <Link to={"/training-plan/"+currentTrainingPlan.id}> Wyświetl szczegóły</Link>
                        </Button>
                        < br />


                  </div> }
               </div>
            </div>
           
           
            <div className="col-xl-12">
               <div className="card">
                  <Tab.Container defaultActiveKey="breakfast">
                     <div className="card-header d-sm-flex d-block pb-0 border-0">
                        <div className="mr-auto pr-3">
                           <h4 className="text-black fs-20">
                             Najpopularniejsze plany treningowe
                           </h4>
                           <p className="fs-13 mb-0 text-black">
                             Polecane dla Ciebie !
                           </p>
                        </div>
                        {/* <div className="card-action card-tabs mt-3 mt-sm-0 mt-3 mb-sm-0 mb-3 mt-sm-0">
                       fajne do wykresow
                        </div> */}
                        <Link
                           to="/community/training-plans"
                           className="btn btn-primary rounded d-none d-lg-block ml-0 ml-md-5"
                        >
                           Zobacz więcej
                        </Link>
                     </div>
                     <div className="card-body">
                        <Tab.Content className="tab-content">
                           <Tab.Pane role="tabpanel" eventKey="breakfast">
                              <div className="featured-menus owl-carousel">
                                { publicPlans !== 'empty' && <Slider {...settings}>
                                    {publicPlans.map((plan, index) =>  {
                                       return (
                                    <div className="items">
                                       <div className="d-sm-flex p-3 border border-light rounded">
                                          <Link to={"/community/training-plan/"+plan.id}>
                                             <img
                                                className="mr-4 public-plan-image rounded"
                                                src={dumbells_icon_s}
                                                alt=""
                                                width={160}
                                             />
                                          </Link>
                                          <div>
           
                                             <h6 className="fs-16 mb-4">
                                                <Link
                                                   to={"/community/training-plan/"+plan.id}
                                                   className="text-black"
                                                >
                                                  {plan.title}
                                                </Link>
                                             </h6>
                                             <ul>
                                                <li className="mb-2">
                                                <i class="fa fa-list-ol" aria-hidden="true"></i>

                                                   <span className="fs-14 text-black">
                                                      Dni treningowych: {plan.numberOfTrainingDays}
                                                   </span>
                                                </li>
                                                <li>
                                                   <i
                                                      className="las la-prescription-bottle mr-3 scale5 text-warning"
                                                      aria-hidden="true"
                                                   />
                                                   <span className="fs-14 text-black font-w500">
                                                      Priorytet: {mapPlanPriority(plan.planPriority)}
                                                   </span>
                                                </li>

<br />
                                                <li>
                                                   <i
                                                      className="fa fa-user mr-3 scale5 text-warning"
                                                      aria-hidden="true"
                                                   />
                                                   <span className="fs-14 text-black font-w500">
                                                   Liczba użytkowników: {plan.planUsersIds.length}
                                                   </span>
                                                </li>
                                         
                                                <li>
                                                   <i
                                                      className="fa fa-star mr-3 scale5 text-warning"
                                                      aria-hidden="true"
                                                   />
                                                   <span className="fs-14 text-black font-w500">
                                                      Średnia ocena: {plan.averageRate}
                                                   </span>
                                                </li>
                                             </ul>
                                          </div>
                                       </div>
                                    </div>) } )}
                                 </Slider> }
                              </div>
                           </Tab.Pane>                  
                        </Tab.Content>
                     </div>
                  </Tab.Container>
               </div>
            </div>
         </div>
      </>
   );
};

export default Home;
