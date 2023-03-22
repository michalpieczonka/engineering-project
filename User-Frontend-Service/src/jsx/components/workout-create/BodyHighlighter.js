import React, {useState} from 'react';
import Model from 'react-body-highlighter';
import { Row, Card, Col } from "react-bootstrap";
import ExerciesPickerModal from './ExerciesPickerModal';
import { useSelector } from 'react-redux';
import swal from "sweetalert";

export default function BodyHighlighter() {
  const state = useSelector((state) => state.workoutwizard)

  const data = [
    { name: 'Bench Press', muscles: ['chest', 'triceps', 'front-deltoids'] },
    { name: 'Push Ups', muscles: ['chest'] },
  ]     

  const[showModal, setShowModal] = useState(false)
  const[muscleGroup, setMuscleGroup] = useState('')
    
  const disableModal = () => {
    setShowModal(false)
  }

  const handleClick = (e) => {
    if (e.muscle !== 'neck' && e.muscle !== 'head' && e.muscle !== 'left-soleus' && e.muscle !== 'knees' && e.muscle !== 'right-soleus') {
      setMuscleGroup(e.muscle)
      if (Array.isArray(state.trainingDays) && !state.trainingDays.length){
        swal('Błąd', 'Nie posiadasz zdefiniowanych dni treningowych. Wróć do poprzedniego kroku i uzupełnij szczegóły', "error");
      } else {
        setShowModal(true)
      }
    }

  }

  return (
    <Row>
       {showModal && <ExerciesPickerModal muscleGroup = {muscleGroup} disableModal = {disableModal}/>} 
             <Col xl={6} className="col-xxl-12">
               <Card>
                  <Card.Header className="d-block">
                     <Card.Title><center>Przód ciała</center></Card.Title>
           
                  </Card.Header>
                  <Card.Body>
                    <center>
                         <Model
                            data={data}
                            onClick={handleClick}
                            highlightedColors={["#000099", "#6971DB"]}
                            style={{ width: '30rem', padding: '5rem', position: 'center' }}
                            />
                    </center>
                  </Card.Body>
               </Card>
            </Col>
            <Col xl={6} className="col-xxl-12">
               <Card>
                  <Card.Header className="d-block">
                     <Card.Title><center>Tył ciała</center></Card.Title>
                  </Card.Header>
                  <Card.Body>
                    <center>
                        <Model
                            type="posterior"
                            data={data}
                            highlightedColors={["#000099", "#6971DB"]}
                            onClick={handleClick}
                            style={{ width: '30rem', padding: '5rem' }} />
                     </center>
                    
                  </Card.Body>
               </Card>
            </Col>
    </Row>   
  );
}