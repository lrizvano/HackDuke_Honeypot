import Foundation

enum HoneypotError: Error {
    case noMessages
    case unknown
    
    var localizedDescription: String {
        switch self {
        case .noMessages:
            return "No messages retrieved"
        default:
            return "An unknown error occured"
        }
    }
}
