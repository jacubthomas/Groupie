import axios from "axios";
import React, { useState, useEffect } from "react";
import '../stylesheets/calendar.css';
import data from "../demoData.json";
// import axios from "axios";

const Calendar = (props) => {

    const url = "/api";
    const { loggedInUsername, handleActivePage, setSelectedEvent } = props;
    const [currentDate, setCurrentDate] = useState(new Date());
	const [selectedDate, setSelectedDate] = useState(new Date());
    const [ baseUserEvents, setBaseEvents ] = useState(data);
    const [ userEvents, setUserEvents ] = useState(data);
    const [ filtered, setFiltered ] = useState(false);
    const [ finalizedFilter, setFinalizedFilter ] = useState(false);
    const [ respondedFilter, setRespondedFilter ] = useState(false);

    const formatMonth = (date) => {
        var strArray=['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        var m = strArray[date.getMonth()];
        var y = date.getFullYear();
        return '' + m + '-' + y;
    }

    const resetEvents = () => {
        setUserEvents(baseUserEvents);
        setFiltered(false);
    }

    const filterBy = () => {
        let eventsArr = [];
        for(let j = 0; j < baseUserEvents.length; j++) {
            if(baseUserEvents[j].finalized === finalizedFilter &&
                baseUserEvents[j].responded === respondedFilter) {
                    eventsArr.push(baseUserEvents[j]);
                }
        }
        setUserEvents(eventsArr);
        setFiltered(true);
    }

    const handleEventPage = (eName) => {
		console.log(eName);
		//console.log(selectedEvent.eventName);
        var selectedEvent = null;
        for(let i = 0; i < userEvents.length; i++) {
            if(userEvents[i].eventName === eName) selectedEvent = userEvents[i];
        }
		console.log(selectedEvent);
        setSelectedEvent(selectedEvent);
		handleActivePage("/inviteeform");
    }

    useEffect(() => {
        setBaseEvents(data);
        axios
            .post(url + "/user/getProposal", loggedInUsername)
            .then((res) => {
                console.log(res);
            })
            .catch((err) => {
                console.log(err);
            })

    }, [setBaseEvents, loggedInUsername]);

    const setHeader = () => {

        return (
            <div className="header row flex-middle">
                <div className="col col-start">
                    <div name="chevron" className="icon" onClick={prevMonth}>
                        chevron_left
                    </div>
                </div>
                <div className="col col-center">
                    <span>
                        {formatMonth(currentDate)}
                    </span>
                </div>
                <div className="col col-end" onClick={nextMonth}>
                    <div className="icon">chevron_right</div>
                </div>
            </div> 
        );
    }

    const setDays = () => {
        console.log("days");
        var weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        const days = [];

        let start = currentDate.getDay();

        for(let i = 0; i < 7; i++) {
            var day = weekdays[(start+i) % 7];
            days.push(<div className="col col-center" key={i}>{day}</div>)
        }

        return <div className="days row">{days}</div>
    }

    const setCells = () => {
        console.log("cells");
        var date = currentDate;
        var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
        const rows = [];
        let days = [];
        let day = firstDay;
        while(day <= lastDay) {
            let textDate = (day.getMonth()+1).toString() + "-" + day.getDate().toString() + "-" + day.getFullYear().toString();
            console.log(textDate);
            let dayHolder = day;
            let eventsHolder = [];
            for(let i = 0; i < userEvents.length; i++) {
                if(userEvents[i].accepted) {
                    if(userEvents[i].startDate === textDate) {
                        console.log(userEvents[i]);
                        eventsHolder.push(userEvents[i]);
                    }
                } else {
                    for(let j = 0; j < userEvents[i].proposedDates.length; j++) {
                        if(userEvents[i].proposedDates[j] === textDate) {
                            console.log(userEvents[i]);
                            eventsHolder.push(userEvents[i]);
                        }
                    }
                }
            }

			let d = day.getDate();
            days.push(
                <div className={`col cell ${dayHolder === selectedDate ? "selected" : ""}`} key={dayHolder} onClick={(e) => onClickDate(e, dayHolder)}>
                    <span className="number">{day.getDate()}</span>
                    { eventsHolder.map((event) => {
                        return (
                            <button name={"cal"+d} type="button" onClick={() => handleEventPage(event.eventName)} className="text-xs text-black-700">{event.eventName}</button>
                        );
                    })}
					
                </div>
            );
            const nextDate = new Date(day.getFullYear(), day.getMonth(), day.getDate()+1);
            day = nextDate;
        }
        rows.push(
            <div className="row" key={day}>
                {days}
            </div>
        );
        days = [];

        return <div className="body">{rows}</div>
    }

    const onClickDate = (event, day) => {
    	//console.log("event: " + event + " | day: " + day);
        setSelectedDate(day);
		console.log(selectedDate);
        //handleActivePage("/inviteeform");
    }

    const nextMonth = () => {
        var newDate = new Date(currentDate.setMonth(currentDate.getMonth()+1));
        setCurrentDate(newDate);
    }

    const prevMonth = () => {
        var newDate = new Date(currentDate.setMonth(currentDate.getMonth()-1));
        setCurrentDate(newDate);
    }

    return (
        <div>
            <header> 
                <div id="logo">
                    <span className="icon">date_range</span>
                    <span name="logo_text"><b>calendar</b></span>
                </div>
            </header>
            <main>
                <div className="flex justify-center mb-4 py-auto">
                <div className="my-auto p-auto">
                        Filter by: 
                    </div>
                    <div className="mx-2 my-2 bg-white p-2 rounded-lg focus:border-4 border-blue">
                        Accepted:
                        <div>
                            <input onClick={() => setFinalizedFilter(true)} type="radio" id="accepted" name="accepted" value={true}
                                    />
                            <label for="accepted">Yes</label>
                        </div>
                        <div>
                            <input onClick={() => setFinalizedFilter(false)}type="radio" id="not-accepted" name="accepted" value={false}
                                    />
                            <label for="not-accepted">No</label>
                        </div>
                    </div>
                    <div className="mx-2 my-2 bg-white p-2 rounded-lg focus:border-4 border-blue">
                        Responded:
                        <div>
                            <input onClick={() => setRespondedFilter(true)} type="radio" id="responded" name="responded" value={true}
                                    />
                            <label for="responded">Yes</label>
                        </div>
                        <div>
                            <input onClick={() => setRespondedFilter(false)}type="radio" id="not-accepted" name="responded" value={false}
                                    />
                            <label for="not-responded">No</label>
                        </div>
                    </div>
                    { !filtered &&
                        <button onClick={() => filterBy()}
                            className="my-auto p-2 px-2 pl-2 pr-2 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300">
                            <b>Filter</b>
                        </button>
                    }
                    { filtered &&
                        <button onClick={() => resetEvents()}
                            className="my-auto p-2 px-2 pl-2 pr-2 bg-blue-500 text-white text-lg rounded-lg focus:border-4 border-blue-300">
                            <b>Reset</b>
                        </button>
                    }
                </div>
                <div className="calendar">
                    {setHeader()}
                    {setDays()}
                    {setCells()}
                </div>
            </main>
        </div>
    );
}

export default Calendar;