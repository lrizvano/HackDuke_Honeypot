import Foundation

struct Response: Codable{
    let status: String
    let message: String
    let data: [Message]
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.message = try container.decode(String.self, forKey: .message)
        self.status = try container.decode(String.self, forKey: .status)
        self.data = try container.decode([Message].self, forKey: .data)
    }
}
