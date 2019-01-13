import { GET_BACKLOG, GET_PROJECT_TASK, DELETE_PROJECT_TASK } from "../actions/types";

//reducer has initial state
const initialState = {
    project_tasks: [], // when fetching list of tasks
    project_task: {} // used when updating one task
};

export default function(state = initialState, action) {
    switch (action.type) {
        case GET_BACKLOG:
            return {
                ...state,
                project_tasks: action.payload
            };

        case GET_PROJECT_TASK:
            return {
                ...state,
                project_task: action.payload
            };

        case DELETE_PROJECT_TASK:
            return {
                ...state
            };

        default:
            return state;
    }
}
