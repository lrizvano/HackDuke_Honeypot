import Foundation

enum HoneypotAPI {
    static func getMessages(_ completion: @escaping (Result<[Message], Error>) -> Void) {
        var request = URLRequest(url: URL(string: "http://35.193.33.133:8080/api/messages")!)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let task = URLSession.shared.dataTask(with: request, completionHandler: { data, response, error -> Void in
            if let error = error {
                completion(.failure(error))
            } else if data == nil {
                completion(.failure(HoneypotError.noMessages))
            } else if let response = try? JSONDecoder().decode(Response.self, from: data!) {
                completion(.success(response.data))
            } else {
                completion(.failure(HoneypotError.noMessages))
            }
        })

        task.resume()
    }
}
