

import React, { useState, useEffect, useRef } from 'react';
import './Hero.css';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js';

// Register Chart.js components
ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

// WebSocket setup URL
const socketURL = '/ws';

const Hero = () => {
  // Form data state
  const [formData, setFormData] = useState({
    id: '',
    customerName: '',
    totalTicket: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maximumNumberOfTickets: '',
  });

  // Error state for form validation
  const [errors, setErrors] = useState({
    customerName: '',
    integers: '',
  });

  // State to store the latest ticket updates from the server
  const [ticketUpdates, setTicketUpdates] = useState({
    totalTickets: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    maximumNumberOfTickets: 0,
  });

  // State for current ticket values displayed on the chart
  const [currentTicketValues, setCurrentTicketValues] = useState({
    totalTickets: 0,
    maximumNumberOfTickets: 0,
  });

  // State to store system messages
  const [messages, setMessages] = useState([]);

  // Refs to store interval IDs for cleanup
  const totalTicketsInterval = useRef(null);
  const maximumTicketsInterval = useRef(null);

  // Initialize WebSocket connection on component mount
  useEffect(() => {
    const stompClient = new Client({
      webSocketFactory: () => new SockJS(socketURL),
      reconnectDelay: 5000,
      debug: (str) => {
        console.log(str);
      },
    });

    stompClient.onConnect = () => {
      console.log('Connected to STOMP over WebSocket');

      // Subscribe to ticket updates
      stompClient.subscribe('/topic/ticketUpdates', (message) => {
        if (message.body) {
          console.log('Received ticket update:', message.body);
          try {
            const update = JSON.parse(message.body);
            setTicketUpdates((prevUpdates) => ({
              totalTickets: update.totalTickets || prevUpdates.totalTickets,
              ticketReleaseRate: update.ticketReleaseRate || prevUpdates.ticketReleaseRate,
              customerRetrievalRate:
                update.customerRetrievalRate !== undefined
                  ? update.customerRetrievalRate
                  : prevUpdates.customerRetrievalRate,
              maximumNumberOfTickets: update.maximumNumberOfTickets || prevUpdates.maximumNumberOfTickets,
            }));
          } catch (error) {
            console.error('Error parsing ticket update:', error);
          }
        }
      });

      // Subscribe to system messages
      stompClient.subscribe('/topic/messages', (message) => {
        if (message.body) {
          console.log('Received message:', message.body);
          setMessages((prevMessages) => [...prevMessages, message.body]);
        }
      });
    };

    stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    stompClient.activate();

    // Cleanup on component unmount
    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
      // Clear any existing intervals
      if (totalTicketsInterval.current) clearInterval(totalTicketsInterval.current);
      if (maximumTicketsInterval.current) clearInterval(maximumTicketsInterval.current);
    };
  }, []);

  // Handle input changes with validation
  const handleChange = (e) => {
    const { name, value } = e.target;

    const integerFields = [
      'id',
      'totalTicket',
      'ticketReleaseRate',
      'customerRetrievalRate',
      'maximumNumberOfTickets',
    ];

    // Validate integer fields
    if (integerFields.includes(name)) {
      if (!/^\d*$/.test(value)) {
        setErrors((prevState) => ({
          ...prevState,
          integers: `${name} must be an integer.`,
        }));
        return;
      } else {
        setErrors((prevState) => ({ ...prevState, integers: '' }));
      }
    }

    // Validate customer name
    if (name === 'customerName') {
      if (/^[a-zA-Z\s]*$/.test(value)) {
        setErrors((prevState) => ({ ...prevState, customerName: '' }));
      } else {
        setErrors((prevState) => ({
          ...prevState,
          customerName: 'Name can only contain letters and spaces.',
        }));
        return;
      }
    }

    // Update form data
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Check for validation errors
    if (errors.customerName || errors.integers) {
      alert('Please fix the errors before submission');
      return;
    }

    try {
      const response = await fetch('/api/v1/customerstest/configure', {
        // Use relative URL due to proxy
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert('Customer Data Submitted Successfully!');
        // Update the customerRetrievalRate in ticketUpdates state
        setTicketUpdates((prevUpdates) => ({
          ...prevUpdates,
          customerRetrievalRate: parseInt(formData.customerRetrievalRate, 10) || 0,
        }));
        // Reset form
        setFormData({
          id: '',
          customerName: '',
          totalTicket: '',
          ticketReleaseRate: '',
          customerRetrievalRate: '',
          maximumNumberOfTickets: '',
        });
      } else {
        alert('Failed to submit');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred');
    }
  };

  // Effect to handle gradual update of ticket values
  useEffect(() => {
    // Function to gradually update a ticket value
    const gradualUpdate = (key, currentValue, targetValue, intervalRef) => {
      // Clear existing interval if any
      if (intervalRef.current) clearInterval(intervalRef.current);

      // Determine the direction of update
      if (currentValue > targetValue) {
        intervalRef.current = setInterval(() => {
          setCurrentTicketValues((prevValues) => {
            const newValue = prevValues[key] - 1;
            if (newValue <= targetValue) {
              clearInterval(intervalRef.current);
              return { ...prevValues, [key]: targetValue };
            }
            return { ...prevValues, [key]: newValue };
          });
        }, 50); // Adjust interval duration for smoothness
      } else if (currentValue < targetValue) {
        intervalRef.current = setInterval(() => {
          setCurrentTicketValues((prevValues) => {
            const newValue = prevValues[key] + 1;
            if (newValue >= targetValue) {
              clearInterval(intervalRef.current);
              return { ...prevValues, [key]: targetValue };
            }
            return { ...prevValues, [key]: newValue };
          });
        }, 50);
      }
      // If equal, do nothing
    };

    // Handle Total Tickets
    gradualUpdate(
      'totalTickets',
      currentTicketValues.totalTickets,
      ticketUpdates.totalTickets,
      totalTicketsInterval
    );

    // Handle Maximum Tickets
    gradualUpdate(
      'maximumNumberOfTickets',
      currentTicketValues.maximumNumberOfTickets,
      ticketUpdates.maximumNumberOfTickets,
      maximumTicketsInterval
    );

    // Cleanup intervals when ticketUpdates change or component unmounts
    return () => {
      if (totalTicketsInterval.current) clearInterval(totalTicketsInterval.current);
      if (maximumTicketsInterval.current) clearInterval(maximumTicketsInterval.current);
    };
  }, [ticketUpdates, currentTicketValues]);

  // Initialize currentTicketValues when component mounts
  useEffect(() => {
    setCurrentTicketValues({
      totalTickets: ticketUpdates.totalTickets,
      maximumNumberOfTickets: ticketUpdates.maximumNumberOfTickets,
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Run only once on mount

  // Prepare data for the bar chart using currentTicketValues
  const chartData = {
    labels: ['Tickets'],
    datasets: [
      {
        label: 'Total Tickets',
        data: [currentTicketValues.totalTickets],
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
      {
        label: 'Maximum Tickets',
        data: [currentTicketValues.maximumNumberOfTickets],
        backgroundColor: 'rgba(255, 99, 132, 0.6)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1,
      },
    ],
  };

  // Chart options for smooth transitions
  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 500, // Duration of the animation in milliseconds
      easing: 'easeOutQuart', // Easing function for the animation
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          precision: 0, // Ensure integer ticks
        },
      },
    },
    plugins: {
      legend: {
        position: 'top',
      },
    },
  };

  return (
    <div className="hero">
      <h1>Ticket Manager Dashboard</h1>

      <div className="content-container">
        {/* System Messages - Fixed Left */}
        <div className="messages-section">
          <h3>System Messages</h3>
          {messages.length > 0 ? (
            messages.map((msg, index) => (
              <p key={index} className="message">
                {msg}
              </p>
            ))
          ) : (
            <p>No messages yet.</p>
          )}
        </div>

        {/* Right Side: Real-Time Data, Bar Chart, and User Input Form */}
        <div className="right-section">
          {/* Real-Time Data */}
          <div className="updates">
            <h3>Real-Time Data</h3>
            <p>
              <strong>Total Tickets:</strong> {currentTicketValues.totalTickets}
            </p>
            <p>
              <strong>Ticket Release Rate:</strong> {ticketUpdates.ticketReleaseRate}
            </p>
            <p>
              <strong>Customer Retrieval Rate:</strong> {ticketUpdates.customerRetrievalRate}
            </p>
            <p>
              <strong>Maximum Tickets:</strong> {currentTicketValues.maximumNumberOfTickets}
            </p>
          </div>

          {/* Bar Chart */}
          <div className="chart-container">
            <Bar data={chartData} options={chartOptions} />
          </div>

          {/* User Input Form */}
          <div className="box">
            <form onSubmit={handleSubmit}>
              <label htmlFor="id">NIC ID</label>
              <input
                id="id"
                name="id"
                type="number"
                placeholder="Enter NIC ID"
                value={formData.id}
                onChange={handleChange}
                required
              />

              <label htmlFor="customerName">Customer Name</label>
              <input
                id="customerName"
                name="customerName"
                type="text"
                placeholder="Enter your name"
                value={formData.customerName}
                onChange={handleChange}
                required
              />
              {errors.customerName && <p className="error">{errors.customerName}</p>}

              <label htmlFor="totalTicket">Total Tickets</label>
              <input
                id="totalTicket"
                name="totalTicket"
                type="number"
                placeholder="Enter total tickets"
                value={formData.totalTicket}
                onChange={handleChange}
              />

              <label htmlFor="ticketReleaseRate">Ticket Release Rate</label>
              <input
                id="ticketReleaseRate"
                name="ticketReleaseRate"
                type="number"
                placeholder="Enter ticket release rate"
                value={formData.ticketReleaseRate}
                onChange={handleChange}
              />

              <label htmlFor="customerRetrievalRate">Customer Retrieval Rate</label>
              <input
                id="customerRetrievalRate"
                name="customerRetrievalRate"
                type="number"
                placeholder="Enter retrieval rate"
                value={formData.customerRetrievalRate}
                onChange={handleChange}
              />

              <label htmlFor="maximumNumberOfTickets">Max Capacity</label>
              <input
                id="maximumNumberOfTickets"
                name="maximumNumberOfTickets"
                type="number"
                placeholder="Enter max tickets"
                value={formData.maximumNumberOfTickets}
                onChange={handleChange}
              />
              {errors.integers && <p className="error">{errors.integers}</p>}

              <button type="submit" className="btn">
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
