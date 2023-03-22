import React, {useEffect, useState, Fragment} from 'react'
import { Link } from "react-router-dom";
import { Button, Modal, Dropdown } from "react-bootstrap";
import PerfectScrollbar from "react-perfect-scrollbar";
import {addTrainingPlanRate} from '../../../services/TrainingPlanService'
import Nouislider from "nouislider-react";
import { store } from '../../../store/store';


const TrainingPlanComments = ({comments, trainingPlanId, refresh}) => {

    const state = store.getState();
    let userId = state.auth.auth.userId;

    const [showModal, setShowModal] = useState(false)
    const [rate, setRate] = useState(0)
    const [comment, setComment] = useState('')
    const commentsLength = comments.length


    const confirmSaveComment = () => {
        let commentData = {
            rateAuthorId: userId,
            rateValue: rate,
            commentText: comment
        }

        addTrainingPlanRate(trainingPlanId, commentData)
        .then(response => {
           if(response.status == 201){
              swal('Sukces', 'Ocena planu treningowego została zapisana, dziękujemy !', "success");
           } else {
              swal('Błąd', 'Nie udało się zapisać oceny planu treningowego, wystąpił błąd.', "error");
           }
           setShowModal(false)
           refresh()
        })
        .catch(error =>{
           swal('Błąd', 'Wystąpił błąd w trakcie zapisu oceny planu treningowego.', "error");
           console.log(error)})

    }

  return (
    <div className="card">
    <div className="card-header d-sm-flex d-block pb-0 border-0">
       <div className="mr-auto pr-3 mb-sm-0 mb-3">
          <h4 className="text-black fs-20"><center>Oceny i komenarze planu treningowego</center></h4>
          <br />
          {commentsLength === 0 &&<p>  <h4> Plan treningowy nie posiada żadnych komentarzy</h4> </p>}
         
       </div>
      <Button variant="primary" className="btn btn-primary btn-lg" data-toggle="modal" data-target="#exampleModalCenter"
      onClick={() => setShowModal(true)}>
        Dodaj komentarz
        </Button>

        
        <Modal className="fade" show={showModal}>
                           <Modal.Header>
                              <Modal.Title>Dodawanie komentarza</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setShowModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
            <center><h3> Oceń plan treningowy</h3>
          <label className="text-label">Jak oceniasz plan treningowy ?<br />Wyraź swoją opinię oceniając plan treningowy poprzez wykorzystanie ponizszego suwaka: </label>
            
            <Nouislider
                              start={0}
                              pips={{ mode: "count", values: 5 }}
                              clickablePips
                              range={{
                                 min: 1,
                                 max: 10,
                              }}
                              onSlide={(e) => setRate(Math.round(e))}
                           />
          <br />
          <br />
          <label className="text-label">Treść komentarza</label>
          <input
                                    value = {comment}
                                     type="text"
                                     name="comments"
                                    className="form-control"
                                    placeholder="Dodaj komentarz - swoje uwagi do planu treningowego"
                                    required
                                    onChange={(e) => setComment(e.target.value)}
                                 /> </center>
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setShowModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => confirmSaveComment()} variant="primary">Zapisz komentarz i ocenę</Button>
                           </Modal.Footer>
                        </Modal> 

    </div>
    <PerfectScrollbar
       className="card-body loadmore-content pb-4 dz-scroll height300"
       id="DietMenusContent"
    >
       {comments.map((d, i) => (
          <div
             className="media mb-3 pb-3 d-md-flex d-block menu-list"
             key={i}
          >

            
             <div className="media-body col-lg-6 pl-0">

                <h7 className="fs-16 ">
 
                    {d.description}
                
                </h7>
                
             </div>
             <ul className="m-md-auto mt-2 pr-4 mb-2">
                <li className="text-nowrap">
                   <i
                      className="fa fa-star-o mr-3 scale5 text-warning"
                      aria-hidden="true"
                   />
                   <span className="text-nowrap fs-14 text-black font-w500">
                      Wystawiona ocena: {d.rate} / 10
                   </span>
                </li>
                <li className="text-nowrap">
                   <i
                      className="fa fa-calendar mr-3 scale5 text-warning"
                      aria-hidden="true"
                   />
                   <span className="text-nowrap fs-14 text-black font-w500">
                      Data oceny: {d.rateDate} 
                   </span>
                </li>
             </ul>
          </div>
       ))}
    </PerfectScrollbar>

 </div>
  )
}

export default TrainingPlanComments
