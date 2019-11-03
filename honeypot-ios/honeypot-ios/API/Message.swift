import Foundation

struct Message: Codable {
    let id: String?
    let time: String
    let msg: String
    let score: Int?
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try? container.decode(String.self, forKey: .id)
        self.time = try container.decode(String.self, forKey: .time)
        self.msg = try container.decode(String.self, forKey: .msg)
        self.score = try? container.decode(Int.self, forKey: .score)
    }
}
