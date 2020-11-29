import './App.css';
import React, { Component } from "react";
import { Button, Form, Col, Row, Modal } from 'react-bootstrap';
import axios from "axios";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = { name: "", lastName: "", tckn: "", monthlyIncome: undefined, phone: "", showModal: false, 
    message: "", showDetail: false, kvkk: false};

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.isValidation = this.isValidation.bind(this);
    this.close = this.close.bind(this);
    this.open = this.open.bind(this);
  }

  handleChange(event) {
    if (event.target.id === "kvkk") {
      this.setState({ kvkk: !this.state.kvkk });
    } else {
      this.setState({ [event.target.id]: event.target.value });
    }
  }

  handleSubmit(event) {
    if (this.isValidation) {
      axios({
        method: 'post',
        url: 'http://localhost:8088/api/create-application',
        data: {
          name: this.state.name,
          lastName: this.state.lastName,
          tckn: this.state.tckn,
          monthlyIncome: this.state.monthlyIncome,
          phone: this.state.monthlyIncome
        }
      }).then(res => {
        let mes = JSON.parse(res.request.response);
        this.setState({ message: mes.message})
        this.setState({ showModal: true });
      }).catch(err => {;
        this.setState({ message: "Lütfen tekrar deneyiniz."})
        this.setState({ showModal: true });
      });
      event.preventDefault();

    } else { 
      this.setState({ message: "Tüm alanların doldurulması zorunludur"})
      this.setState({ showModal: true });
    }
  }
  close() {
    this.setState({ showModal: false });
  }

  open() {
    this.setState({ showModal: true });
  }
 

  isValidation() {
    if (this.state.name !== "" && this.state.lastName !== "" && this.state.tckn !== "" && this.state.kvkk === true
      && (this.state.monthlyIncome !== "" || this.state.monthlyIncome !== undefined) && (this.state.phone !== "" && this.state.phone.length >= 10)) {
      return true;
    }
    return false;
  }

  render() {
    return (
      <div className="wrappper">
        <Row>
          <Col xs={10} md={7} lg={6} className="mainColumn">
            <h2 className="title">Kredi Başvuru Sistemi</h2>
            <div className="clr10"></div>
            <Form onSubmit={this.handleSubmit}>
              <Form.Group controlId="name">
                <Form.Control type="name" onChange={this.handleChange} required placeholder="Ad" />
              </Form.Group>
              <Form.Group controlId="lastName">
                <Form.Control type="lastName" onChange={this.handleChange} required placeholder="Soyad" />
              </Form.Group>
              <Form.Group controlId="monthlyIncome">
                <Form.Control type="monthlyIncome" onChange={this.handleChange} required placeholder="Aylık Gelir" />
              </Form.Group>
              <Form.Group controlId="tckn">
                <Form.Control type="tckn" onChange={this.handleChange} required placeholder="TCKN" />
              </Form.Group>
              <Form.Group controlId="phone">
                <Form.Control type="phone" onChange={this.handleChange} pattern="[0-9]{10}" required placeholder="Cep Telefonu" />
                <Form.Text className="text-muted">
                  Telefon numaranızı başında alan kodu olmadan "5452132323" şekilinde giriniz
                </Form.Text>
              </Form.Group>
              <Form.Group controlId="kvkk">
                <Form.Check type="checkbox" required onChange={this.handleChange} label="Başvuru koşullarını onaylıyorum." />
              </Form.Group>
              <Button variant="primary" style={{cursor: "pointer"}} type="submit" size="md" block>Onay</Button>
            </Form>
          </Col>
        </Row>
        <Modal show={this.state.showModal} onHide={this.close}>
          <Modal.Header closeButton>
            <Modal.Title>Başvuru Sonucu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <h4>{this.state.message}</h4>

          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.close}>Close</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}

export default App;