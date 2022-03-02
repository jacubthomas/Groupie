import React, { useState } from "react";
import axios from "axios";

const EventForm = (props) => {
    const url = "/api";

    const { loggedInUsername, handleActivePage } = props;
    const [ usersInvited, setUsersInvited ] = useState([]);
    const [ invited, setInvited ] = useState(false);
    const [ user, setUser ] = useState("");
    const [ eventCity, setCity ] = useState("");
    const [ postalCode, setPostalCode ] = useState(0);
    const [ eventDistance, setDistance ] = useState(0);
    const [ eventStartTime, setStartTime ] = useState(0);
    const [ eventEndDate, setEndDate ] = useState(new Date());
    const [ eventEndTime, setEndTime ] = useState(0);
    const [ searchTerm, setSearchTerm ] = useState("");
    const [ searched, setSearched ] = useState(false);
    const [ listedEvents, setEvents ] = useState([]);
    const [ invalidUser, setInvalidUser ] = useState(false);
    /*const [ searchEvent, setSearchEvent ] = useState({
      city: "",
      postCode: 0,
      distance: 0,
      startDate: "",
      startTime: "",
      endDate: "",
      endTime: "",
      keyword: ""
    }); */
    const [ selectedEvents, setSelectedEvents ] = useState([]);
    //const [ selectedEventName, setSEN ] = useState("");
    const [ noElements, setNoElements ] = useState(false);
    //const [ noPostalCode, setNoPostalCode ] = useState(false);

    function toggleEvent(eventName, eventDate) {
      const id = eventName + "-" + eventDate;
      let grabEvent = listedEvents[0];
      for(let i = 0; i < listedEvents.length; i++) {
        if(listedEvents[i].name === eventName && listedEvents[i].dates.start.localDate === eventDate) grabEvent = listedEvents[i];
      }
      let eventsArr = [];
      if(document.getElementById(id).classList.contains("border-green-600")) {
        document.getElementById(id).classList.remove("border-green-600");
        for(let j = 0; j < selectedEvents.length; j++) {
          console.log(eventName + "-" + eventDate);
          if(selectedEvents[j].title === eventName && selectedEvents[j].startDate === eventDate) {
            // 
          } else {
            eventsArr.push(selectedEvents[j]);
          }
        }
        setSelectedEvents(eventsArr);
      } else {
        document.getElementById(id).classList.add("border-green-600");
        const eventToAdd = {
          title: grabEvent.name,
          eventLocation: grabEvent._embedded.venues[0].city.name + " " + grabEvent._embedded.venues[0].state.name,
          startDate: grabEvent.dates.start.localDate,
          endDate: grabEvent.dates.start.localDate,
          startTime: grabEvent.dates.start.localTime,
          endTime: "",
          genre: grabEvent.classifications[0].genre.name,
          keyword: grabEvent.classifications[0].segment.name
        };
        eventsArr = selectedEvents;
        eventsArr.push(eventToAdd);
        setSelectedEvents(eventsArr);
      }
    }

    function addUser(u) {
      axios 
        .get(url + "/user/username/" + u)
        .then((res) => {
          console.log(res);
          setInvalidUser(false);
          const newArr = usersInvited;
          if(!newArr.includes(u)) newArr.push(u);
          setUsersInvited(newArr);
          setInvited(true);
          document.getElementById("username").value = "";
          setUser("");
        })
        .catch((err) => {
          console.log(err);
          setInvalidUser(true);
        });
        
    }

    function searchEvents() {
      var startTimeString = eventStartTime.toString();
      var endTimeString = eventEndTime.toString();
      if(eventStartTime === 0) {
        startTimeString = "00:00";
      }
      if(eventEndTime === 0) {
        endTimeString = "00:00";
      }
      startTimeString += ":00";
      endTimeString += ":00";
      var date = new Date();
      var startDateString = date.getFullYear().toString() + "-" + (date.getMonth() + 1).toString() + "-";
      var day = date.getDate().toString();
      if(day.length === 1) {
        startDateString += "0";
      }
      startDateString += day;
      //var endDateString = eventEndDate.getFullYear().toString() + "-" + (eventEndDate.getMonth() + 1).toString() + "-" + eventEndDate.getDate().toString();
      const newSearchEvent = {
        city: eventCity,
        postCode: postalCode,
        distance: eventDistance,
        distanceUnit: "miles",
        startDate: startDateString,
        startTime: startTimeString,
        endDate: eventEndDate,
        endTime: endTimeString,
        keyword: searchTerm,
        page_num: "0",
        page_size: "10"
      };
      console.log(newSearchEvent);
      //setSearchEvent(newSearchEvent);


      axios 
        .post(url + "/searchEvent/",newSearchEvent)
        .then((res) => {
          console.log(res);
          if(res.data.page.totalElements === 0) {
            setNoElements(true);
          } else {
            setEvents(res.data._embedded.events);
            setNoElements(false);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      
      setSearched(true);
    }

    function createEvent() {
      // TODO: after create event endpoint'
      var inviteeArray = [];
      for(let i = 0; i < usersInvited.length; i++) {
        inviteeArray.push({
          username: usersInvited[i],
          userAsInvitees: [],
          ownedProposals: []
        })
      }
      
      const proposalInfo = {
        proposer: {
          username: loggedInUsername
        },
        invitees: inviteeArray,
        proposal_name: "",
        events: selectedEvents
      }
      console.log(proposalInfo);
      
      axios
        .post(url + "/user/proposer/createProposal", proposalInfo)
        .then((res) => {
          console.log(res)
        })
        .catch((err) => {
          console.log(err);
        })
    } 

    return (
      <div className="flex flex-col justify-center my-10">
        <div className="mx-auto w-11/12 max-w-md">
          <h1 className="text-black text-xl font-sans text-center">
            Create Proposal
          </h1>
          <form onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            
          }}>
            <div className="bg-white rounded px-8 pt-6 pb-8 mb-4 flex flex-col">
                <div className="mb-4">
                    <div className="block text-grey-darker text-lg font-bold mb-2 text-left">
                        Search Events
                    </div>
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       City: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="city"
                    type="text"
                    onChange={(e) => setCity(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       Postal Code: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="postalcode"
                    type="text"
                    onChange={(e) => setPostalCode(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       Distance (Miles): 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="distance"
                    type="text"
                    onChange={(e) => setDistance(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       Start Time: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="distance"
                    type="time"
                    onChange={(e) => setStartTime(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       End Date: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="distance"
                    type="date"
                    onChange={(e) => setEndDate(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       End Time: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="distance"
                    type="time"
                    onChange={(e) => setEndTime(e.target.value)}
                    />
                    <label
                        className="block text-grey-darker text-sm font-bold mb-2 text-left"
                        htmlFor="user"
                        >
                       Search Term: 
                    </label>
                    <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="distance"
                    type="text"
                    onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <button
                      onClick={() => searchEvents()} 
                      className="p-2 pl-5 pr-5 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300 w-full">
                        <b>Search Events</b>
                    </button>
                </div>
              { searched &&
                <div className="mb-4">
                  { listedEvents.map((e) => {
                    return (
                      <div 
                      onClick={() => toggleEvent(e.name, e.dates.start.localDate)}
                      id={e.name + "-" + e.dates.start.localDate}
                      className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2">
                          <p className="text-md text-black-700">{e.name}</p>
                          <p className="text-md text-black-700">{e.dates.start.localDate} - {e.dates.start.localTime}</p>
                      </div>
                    );
                  })}
                </div>
              }
              { noElements && 
                <div className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2">
                    <p className="text-md text-black-700">There were no events in your search. </p>
                </div>

              }
              <div className="mb-4">
                <label
                  className="block text-grey-darker text-sm font-bold mb-2 text-left"
                  htmlFor="user"
                >
                  Add users: 
                </label>
                <input
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker mb-2"
                    id="username"
                    type="text"
                    onChange={(e) => setUser(e.target.value)}
                    /> 
                    { invalidUser && 
                      <div class="text-xs text-red-700" id="user">Username not found in database. </div>
                    }
                  { invited && usersInvited.map((user) => {
                      return <div class="text-xs text-green-700" id="user">User "{user}" has been invited. </div>
                  })}
                  <button 
                  onClick={() => addUser(user)}
                  className="p-2 pl-2 pr-4 bg-gray-400 text-gray-100 text-lg rounded-lg focus:border-4 border-gray-300">
                    <b>Add User</b>
                  </button>
              </div>
              <div className="grid grid-cols-1 gap-4">
                <button onClick={(e) => createEvent()}
                  className="p-2 pl-5 pr-5 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300 w-full"
                >
                  <b>Create Event</b>
                </button>
                <button
                  className="p-2 pl-5 pr-5 bg-gray-400 text-gray-100 text-lg rounded-lg focus:border-4 border-gray-300 w-full"
                  onClick={(e) => {
                    e.stopPropagation();
                    e.preventDefault();
                    handleActivePage("/calendar");
                  }}
                >
                  <b>Cancel</b>
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    );
}

export default EventForm;
