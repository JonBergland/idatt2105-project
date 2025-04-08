import type { User } from "@/models/user";
import type { ItemResponseDTO } from "@/models/item";

export interface Message {
  messageID: number;
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
