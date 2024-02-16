import Stomp from "stompjs";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import SockJS from "sockjs-client/dist/sockjs";
import {useAppDispatch, useAppSelector} from "../store/hooks.ts";
import {setIsConnected, appendMessage,setStompClient } from "../store/Slices/webSocketSlice.ts";
import {selectWebSocket} from "../store/Slices/webSocketSlice.ts";

function useWebSocket(){
    const dispatch = useAppDispatch()
    const webSocket = useAppSelector(selectWebSocket)
    // Implement connect
    // it receives "username"
    function connect(username : string){
        try {
            // create socket to connect the server
            const socket: WebSocket = new SockJS(`http://localhost:8080/ws`);
            const stompClient: Stomp.Client = Stomp.over(socket); // this will saved later
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            stompClient.connect({}, () =>onConnected(stompClient,username), onError); // call-back function
        } catch (e) {
            console.log(e);
        }
    }

    function sendMessage(message : string, username : string){
        if (webSocket.stompClient && webSocket.stompClient.connected) {
            const chatMessage = {
                sender: username,
                content: message,
                timestamp: new Date().toLocaleTimeString(),
                type: 'CHAT'
            };
            webSocket.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        }
    }

    // this function is called what connecting is success
    const onConnected = (stompClient : Stomp.Client, username : string) => {
    // then user will subscribe to '/topic/public'
        stompClient.subscribe('/topic/public', onMessageReceived); // here is whem user receive message then onMessageReceived(callback func.) is called
    // then send message that this user is joined
    // 'content' is not necessary here since it's just only joim message
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN', timestamp: new Date().toLocaleTimeString() }));
        dispatch(setIsConnected(true)) // บอกตัวเอง(?) ว่าconnectผ่านแล้วนะ
        dispatch(setStompClient(stompClient)) // save the connection client for sending massage in later than this
    }

    // this function is called when user receive message
    const onMessageReceived = (payload : Stomp.Message) => {
        dispatch(appendMessage(JSON.parse(payload.body))) // เก็บ แล้วก็เอามาต่อๆๆ เรียงกันลงมา
    }
    return {connect,sendMessage}
}

export default useWebSocket;

const onError = (err: Stomp.Message) => {
    console.log(err);
}