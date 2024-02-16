import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store.ts";
import Stomp from "stompjs";

export enum messageType {
    CHAT = 'CHAT',
    JOIN = 'JOIN',
    LEAVE = 'LEAVE'
}

interface WebSocketMessage {
    sender: string;
    content: string;
    timestamp: string;
    type: messageType;
    count: string;
}

interface WebSocketState {
    isConnected: boolean;
    stompClient: Stomp.Client | undefined;
    messages: WebSocketMessage[] | undefined;
}

const initialState: WebSocketState = {
    isConnected: false,
    stompClient: undefined,
    messages: [],

};

export const webSocketSlice = createSlice({
    name: 'webSocket',
    initialState,
    reducers: {
        setIsConnected: (state, action: PayloadAction<boolean>) => {
            state.isConnected = action.payload;
        },
        appendMessage: (state, action: PayloadAction<WebSocketMessage>) => {
            state.messages?.push(action.payload);
        },
        setStompClient: (state, action: PayloadAction<Stomp.Client>) => {
            state.stompClient = action.payload;
        },

    },
});

export const { setIsConnected, appendMessage, setStompClient} = webSocketSlice.actions;
export default webSocketSlice.reducer;

// Selector to retrieve the entire WebSocket state
export const selectWebSocket = (state: RootState) => state.webSocket;
