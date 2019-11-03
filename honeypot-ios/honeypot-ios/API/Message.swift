import Foundation

struct Message: Codable {
    let id: String?
    let time: String
    let msg: String
    let score: Int?
    
    init?(dictionary: [String: Any]) {
        guard let id = dictionary["id"] as? String,
            let time = dictionary["time"] as? String,
            let message = dictionary["msg"] as? String else {
                return nil
        }
        
        self.id = id
        self.time = time
        self.msg = message
        self.score = dictionary["score"] as? Int
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try? container.decode(String.self, forKey: .id)
        self.time = try container.decode(String.self, forKey: .time)
        self.msg = try container.decode(String.self, forKey: .msg)
        self.score = try? container.decode(Int.self, forKey: .score)
    }
    
    init?(json: String) {
        self.init(data: Data(json.utf8))
    }
    
    init?(data: Data) {
        guard let json = (try? JSONSerialization.jsonObject(with: data)) as? [String: Any] else { return nil }
        self.init(dictionary: json)
    }
}
