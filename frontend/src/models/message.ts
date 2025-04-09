import type { User } from "@/models/user";
import type { ItemResponseDTO } from "@/models/item";

export interface Message {
  messageID: number;
  senderID: number;         // The ID of the user that sent the message
  message: string;
  notSeenByUser: boolean; // Is true when the user has not seen the message
  published: string; // Datetime of the object?
}

export interface ChatResponseDTO {
  messages: Message[];
  buyer: User;
  seller: User;
  item: ItemResponseDTO; // The item the chat is about
}

export interface ChatsResponseDTO {
  chats: ChatResponseDTO[]
}
